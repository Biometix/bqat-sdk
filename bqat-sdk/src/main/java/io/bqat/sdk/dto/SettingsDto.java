package io.bqat.sdk.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SettingsDto {
	private String jsonKeyFingerQualityScore;
	private String jsonKeyIrisQualityScore;
	private String jsonKeyFaceQualityScore;

	private String serverIpAddress;
	private String serverPort;
	private String serverPath;
	private String contentType;
	private String contentCharset;
	private String jsonResults;
	private String engine;
	private String timestamp;

	@SuppressWarnings({ "java:S107" })
	public SettingsDto(String jsonKeyFingerQualityScore, String jsonKeyIrisQualityScore, String jsonKeyFaceQualityScore,
			String serverIpAddress, String serverPort, String serverPath, String contentType, String contentCharset,
			String jsonResults, String engine, String timestamp) {
		super();
		this.jsonKeyFingerQualityScore = jsonKeyFingerQualityScore;
		this.jsonKeyIrisQualityScore = jsonKeyIrisQualityScore;
		this.jsonKeyFaceQualityScore = jsonKeyFaceQualityScore;
		this.serverIpAddress = serverIpAddress;
		this.serverPort = serverPort;
		this.serverPath = serverPath;
		this.contentType = contentType;
		this.contentCharset = contentCharset;
		this.jsonResults = jsonResults;
		this.engine = engine;
		this.timestamp = timestamp;
	}
}
