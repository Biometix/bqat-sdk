package io.bqat.sdk.constant;

public class SDKContstant {
	private SDKContstant() {
		throw new IllegalStateException("SDKContstant class");
	}

	public static final String MODALITY_FACE = "face";
	public static final String MODALITY_IRIS = "iris";
	public static final String MODALITY_FINGERPRINT = "fingerprint";

	public static final String DATATYPE_JP2 = "jp2";
	public static final String DATATYPE_WSQ = "wsq";
}