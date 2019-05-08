package com.jastao.oauthservice.services;

import com.jastao.oauthservice.repository.AuthClientRepository;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * Created by JT on 4/21/2019
 */
@Service
public class AuthClientDetailsService implements ClientDetailsService {

	private final AuthClientRepository authClientRepository;
	
	public AuthClientDetailsService( AuthClientRepository authClientRepository ) {
		this.authClientRepository = authClientRepository;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		
		return authClientRepository.findByClientId(clientId)
					.orElseThrow(IllegalArgumentException::new);
	}
}
