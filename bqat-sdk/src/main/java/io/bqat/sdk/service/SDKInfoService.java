package io.bqat.sdk.service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.mosip.kernel.biometrics.constant.BiometricFunction;
import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.model.SDKInfo;

@Component
public class SDKInfoService extends SDKService {
	private String apiVersion;
	private String sdkVersion;
	private String organization;
	private String type;

	public SDKInfoService(String apiVersion, String sdkVersion, String organization, String type) {
		this.apiVersion = apiVersion;
		this.sdkVersion = sdkVersion;
		this.organization = organization;
		this.type = type;
	}

	public SDKInfo getSDKInfo() {
		SDKInfo sdkInfo = new SDKInfo(this.apiVersion, this.sdkVersion, this.organization, this.type);
		List<BiometricType> supportedModalities = new ArrayList<>();
		supportedModalities.add(BiometricType.FINGER);
		supportedModalities.add(BiometricType.FACE);
		supportedModalities.add(BiometricType.IRIS);
		sdkInfo.setSupportedModalities(supportedModalities);
		Map<BiometricFunction, List<BiometricType>> supportedMethods = new EnumMap<>(BiometricFunction.class);
		supportedMethods.put(BiometricFunction.QUALITY_CHECK, supportedModalities);
		sdkInfo.setSupportedMethods(supportedMethods);
		return sdkInfo;
	}
}