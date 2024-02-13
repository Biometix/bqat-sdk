package io.bqat.sdk.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.model.MatchDecision;
import io.mosip.kernel.biometrics.model.QualityCheck;
import io.mosip.kernel.biometrics.model.Response;
import io.mosip.kernel.biometrics.model.SDKInfo;
import io.mosip.kernel.biometrics.spi.IBioApiV2;
import io.bqat.sdk.dto.SettingsDto;
import io.bqat.sdk.service.CheckQualityService;
import io.bqat.sdk.service.SDKInfoService;
import io.bqat.sdk.service.SDKServiceHelper;

/**
 * The Class BqatQualitySDKService.
 * 
 * @author Janardhan B S
 * 
 */
@Component
@EnableAutoConfiguration
public class BqatQualitySDKService implements IBioApiV2 {
	Logger LOGGER = LoggerFactory.getLogger(BqatQualitySDKService.class);

	private static final String API_VERSION = "0.9";
	private static final String SDK_VERSION = "1.0";
	private static final String ORGINALIZATION = "BQAT";
	private static final String TYPE = "QUALITY";
	
	@Value("${bqat.jsonkey.finger.quality.score}")
	private String getJsonKeyFingerQualityScore;

	@Value("${bqat.jsonkey.iris.quality.score}")
	private String getJsonKeyIrisQualityScore;

	@Value("${bqat.jsonkey.face.quality.score}")
	private String getJsonKeyFaceQualityScore;

	@Value("${bqat.server.ipaddress}")
	private String getServerIpAddress;

	@Value("${bqat.server.port}")
	private String getServerPort;

	@Value("${bqat.server.path}")
	private String getServerPath;

	@Value("${bqat.content.type}")
	private String getContentType;

	@Value("${bqat.content.charset}")
	private String getContentCharset;

	@Value("${bqat.json.result}")
	private String getJsonResults;

	@Override
	public SDKInfo init(Map<String, String> initParams) {
		// TODO validate for mandatory initParams
		SDKInfoService service = new SDKInfoService(API_VERSION, SDK_VERSION, ORGINALIZATION, TYPE);
		return service.getSDKInfo();
	}

	@Override
	public Response<QualityCheck> checkQuality(BiometricRecord sample, List<BiometricType> modalitiesToCheck,
			Map<String, String> flags) {
		SettingsDto settingsDto = new SettingsDto(getJsonKeyFingerQualityScore, getJsonKeyIrisQualityScore, getJsonKeyFaceQualityScore, 
				getServerIpAddress, getServerPort, getServerPath, getContentType, getContentCharset, getJsonResults);
		CheckQualityService service = new CheckQualityService(sample, modalitiesToCheck, flags, settingsDto);
		return service.getCheckQualityInfo();
	}

	@Override
	public Response<MatchDecision[]> match(BiometricRecord sample, BiometricRecord[] gallery,
			List<BiometricType> modalitiesToMatch, Map<String, String> flags) {
		return null;
	}

	@Override
	public Response<BiometricRecord> extractTemplate(BiometricRecord sample, List<BiometricType> modalitiesToExtract,
			Map<String, String> flags) {
		return null;
	}

	@Override
	public Response<BiometricRecord> segment(BiometricRecord sample, List<BiometricType> modalitiesToSegment,
			Map<String, String> flags) {
		return null;
	}

	@Override
	public Response<BiometricRecord> convertFormatV2(BiometricRecord sample, String sourceFormat, String targetFormat,
			Map<String, String> sourceParams, Map<String, String> targetParams,
			List<BiometricType> modalitiesToConvert) {
		return null;
	}

	@Override
	@Deprecated
	public BiometricRecord convertFormat(BiometricRecord sample, String sourceFormat, String targetFormat,
			Map<String, String> sourceParams, Map<String, String> targetParams,
			List<BiometricType> modalitiesToConvert) {
		return null;
	}
}
