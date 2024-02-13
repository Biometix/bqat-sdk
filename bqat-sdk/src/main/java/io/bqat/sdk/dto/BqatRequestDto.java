package io.bqat.sdk.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BqatRequestDto {
	private String modality;
	private String id; 
	private String type;
	private String data; //base64UrlEncodedData
}
