package io.bqat.sdk.service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.bqat.sdk.constant.ResponseStatus;
import io.bqat.sdk.dto.BqatRequestDto;
import io.bqat.sdk.dto.SettingsDto;
import io.bqat.sdk.exceptions.SDKException;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.model.QualityCheck;
import io.mosip.kernel.biometrics.model.QualityScore;
import io.mosip.kernel.biometrics.model.Response;

@Component
public class CheckQualityService extends SDKService {	
	private SettingsDto settingsDto;

	private BiometricRecord sample;
	private List<BiometricType> modalitiesToCheck;
	@SuppressWarnings("unused")
	private Map<String, String> flags;

	private Logger logger = LoggerFactory.getLogger(CheckQualityService.class);
	private SDKServiceHelper serviceHelper;
	
	public CheckQualityService(BiometricRecord sample, List<BiometricType> modalitiesToCheck,
			Map<String, String> flags, SettingsDto settingsDto) {
		this.sample = sample;
		this.modalitiesToCheck = modalitiesToCheck;
		this.flags = flags;
		this.settingsDto = settingsDto;
		
		serviceHelper = new SDKServiceHelper(settingsDto);
	}

	public Response<QualityCheck> getCheckQualityInfo() {
		ResponseStatus responseStatus = null;
		Map<BiometricType, QualityScore> scores = null;
		Response<QualityCheck> response = new Response<>();
		try {
			if (sample == null || sample.getSegments() == null || sample.getSegments().isEmpty()) {
				responseStatus = ResponseStatus.MISSING_INPUT;
				throw new SDKException(responseStatus.getStatusCode() + "", responseStatus.getStatusMessage());
			}

			scores = new EnumMap<>(BiometricType.class);
			Map<BiometricType, List<BIR>> segmentMap = getBioSegmentMap(sample, modalitiesToCheck);
			for (Map.Entry<BiometricType, List<BIR>> entry : segmentMap.entrySet()) {
				BiometricType modality = entry.getKey();
				QualityScore qualityScore = evaluateQuality(modality, segmentMap.get(modality));
				scores.put(modality, qualityScore);
			}
		} catch (SDKException ex) {
			logger.error("checkQuality -- error", ex);
			switch (ResponseStatus.fromStatusCode(Integer.parseInt(ex.getErrorCode()))) {
			case INVALID_INPUT:
				response.setStatusCode(ResponseStatus.INVALID_INPUT.getStatusCode());
				response.setStatusMessage(ResponseStatus.INVALID_INPUT.getStatusMessage() + " sample");
				response.setResponse(null);
				return response;
			case MISSING_INPUT:
				response.setStatusCode(ResponseStatus.MISSING_INPUT.getStatusCode());
				response.setStatusMessage(ResponseStatus.MISSING_INPUT.getStatusMessage() + " sample");
				response.setResponse(null);
				return response;
			case QUALITY_CHECK_FAILED:
				response.setStatusCode(ResponseStatus.QUALITY_CHECK_FAILED.getStatusCode());
				response.setStatusMessage(ResponseStatus.QUALITY_CHECK_FAILED.getStatusMessage());
				response.setResponse(null);
				return response;
			case BIOMETRIC_NOT_FOUND_IN_CBEFF:
				response.setStatusCode(ResponseStatus.BIOMETRIC_NOT_FOUND_IN_CBEFF.getStatusCode());
				response.setStatusMessage(
						ResponseStatus.BIOMETRIC_NOT_FOUND_IN_CBEFF.getStatusMessage());
				response.setResponse(null);
				return response;
			case MATCHING_OF_BIOMETRIC_DATA_FAILED:
				response.setStatusCode(ResponseStatus.MATCHING_OF_BIOMETRIC_DATA_FAILED.getStatusCode());
				response.setStatusMessage(
						ResponseStatus.MATCHING_OF_BIOMETRIC_DATA_FAILED.getStatusMessage());
				response.setResponse(null);
				return response;
			case POOR_DATA_QUALITY:
				response.setStatusCode(ResponseStatus.POOR_DATA_QUALITY.getStatusCode());
				response.setStatusMessage(ResponseStatus.POOR_DATA_QUALITY.getStatusMessage());
				response.setResponse(null);
				return response;
			default:
				response.setStatusCode(ResponseStatus.UNKNOWN_ERROR.getStatusCode());
				response.setStatusMessage(ResponseStatus.UNKNOWN_ERROR.getStatusMessage());
				response.setResponse(null);
				return response;
			}
		} catch (Exception ex) {
			logger.error("checkQuality -- error", ex);
			response.setStatusCode(ResponseStatus.UNKNOWN_ERROR.getStatusCode());
			response.setStatusMessage(ResponseStatus.UNKNOWN_ERROR.getStatusMessage());
			response.setResponse(null);
			return response;
		}

		response.setStatusCode(ResponseStatus.SUCCESS.getStatusCode());
		response.setStatusMessage(ResponseStatus.SUCCESS.getStatusMessage());
		QualityCheck check = new QualityCheck();
		check.setScores(scores);
		response.setResponse(check);

		return response;
	}

	private QualityScore evaluateQuality(BiometricType modality, List<BIR> segments) throws JSONException {
		QualityScore score = new QualityScore();
		List<String> errors = new ArrayList<>();
		score.setScore(0);
		switch (modality) {
		case FACE:
			return evaluateQuality(segments);
		case FINGER:
			return evaluateQuality(segments);
		case IRIS:
			return evaluateQuality(segments);
		default:
			errors.add("Modality " + modality.name() + " is not supported");
		}
		score.setErrors(errors);
		return score;
	}

	private QualityScore evaluateQuality(List<BIR> segments) throws JSONException {
		QualityScore score = new QualityScore();
		score.setAnalyticsInfo(new HashMap<>());
		List<String> errors = new ArrayList<>();
		setAvgQualityScore(segments, score, errors);
		score.setErrors(errors);
		return score;
	}

	@SuppressWarnings({ "java:S135", "java:S3776", "unused", "unchecked" })
	private void setAvgQualityScore(List<BIR> segments, QualityScore score, List<String> errors) throws JSONException {
		float qualityScore = 0;
		int segmentCount = 0;
		for (BIR segment : segments) {
			BqatRequestDto requestDto = new BqatRequestDto ();
			if (!isValidBirData(segment, requestDto))
				break;
			segmentCount++;
			JSONObject jsonObject = serviceHelper.getQualityInfoWithJson(requestDto);
			JSONObject jsonObjectResults = jsonObject.getJSONObject(settingsDto.getJsonResults());
			score.getAnalyticsInfo().put(settingsDto.getEngine(), jsonObject.get(settingsDto.getEngine()).toString());
			score.getAnalyticsInfo().put(settingsDto.getTimestamp(), jsonObject.get(settingsDto.getTimestamp()).toString());

			Iterator<String> listKEY = jsonObjectResults.keys();
		    while (listKEY.hasNext()) {
		        String key = listKEY.next();
		        String value = null;
				try {
					value = jsonObjectResults.get(key).toString();
			        score.getAnalyticsInfo().put(key, value);
			        
			        switch (requestDto.getModality())
			        {
				        case "fingerprint":
				        	if (key.equalsIgnoreCase(settingsDto.getJsonKeyFingerQualityScore()))
				        		qualityScore = Float.parseFloat(value);
				        	break;
				        case "iris":
				        	if (key.equalsIgnoreCase(settingsDto.getJsonKeyIrisQualityScore()))
				        		qualityScore = Float.parseFloat(value);
				        	break;
				        case "face":
				        	if (key.equalsIgnoreCase(settingsDto.getJsonKeyFaceQualityScore()))
				        		qualityScore = Float.parseFloat(value);
				        	break;
			        	default:
				        	break;
			        }
			        score.setScore(qualityScore);
				} catch (JSONException e) {
					errors.add(e.getLocalizedMessage());
				}
		    }
		    if (segmentCount > 0)
		    	break;//one segment data at a time
		}
	}
}