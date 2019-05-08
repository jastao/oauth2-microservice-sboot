package com.jastao.oauthservice.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.SerializationUtils;

/**
 * Created by JT on 4/23/2019
 */
public final class ObjectMapper {
	
	private ObjectMapper() {}
	
	public static final OAuth2Authentication deserialize(String encodedObject) {
	
		byte[] bytes = Base64.decodeBase64(encodedObject);
		return (OAuth2Authentication) SerializationUtils.deserialize(bytes);
	}
	
	public static final String serialize(OAuth2Authentication auth2Authentication) {
	
		byte[] bytes = SerializationUtils.serialize(auth2Authentication);
		return Base64.encodeBase64String(bytes);
	}
}
