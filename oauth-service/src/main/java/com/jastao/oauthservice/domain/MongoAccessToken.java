package com.jastao.oauthservice.domain;

import com.jastao.oauthservice.util.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * Created by JT on 4/23/2019
 */
@Document
public class MongoAccessToken {
	
	public static final String TOKEN_ID = "tokenId";
	public static final String REFRESH_TOKEN = "refreshToken";
	public static final String AUTHENTICATION_ID = "authenticationId";
	public static final String CLIENT_ID = "clientU=Id";
	public static final String USERNAME = "username";
	
	@Id
	private Long id;
	
	private String tokenId;
	private OAuth2AccessToken token;
	private String authenticationId;
	private String clientId;
	private String username;
	private String authentication;
	private String refreshToken;
	
	
	public void setAuthentication(OAuth2Authentication authentication) {
		this.authentication = ObjectMapper.serialize(authentication);
	}

	public OAuth2Authentication getAuthentication() {
		return ObjectMapper.deserialize(this.authentication);
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public OAuth2AccessToken getToken() {
		return token;
	}

	public void setToken(OAuth2AccessToken token) {
		this.token = token;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
