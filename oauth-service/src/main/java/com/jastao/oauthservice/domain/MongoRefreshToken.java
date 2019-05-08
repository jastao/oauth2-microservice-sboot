package com.jastao.oauthservice.domain;

import com.jastao.oauthservice.util.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * Created by JT on 4/23/2019
 */
@Getter
@Setter
@Document
public class MongoRefreshToken {

	public static final String TOKEN_ID = "tokenId";

	@Id
	private Long id;

	private String tokenId;
	private OAuth2RefreshToken token;
	private String authentication;

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

	public OAuth2RefreshToken getToken() {
		return token;
	}

	public void setToken(OAuth2RefreshToken token) {
		this.token = token;
	}
}
