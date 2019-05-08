package com.jastao.oauthservice.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by JT on 4/21/2019
 */
public enum Authorities implements GrantedAuthority {
	ROLE_USER;
	
	@Override
	public String getAuthority() {
		return name();
	}
}
