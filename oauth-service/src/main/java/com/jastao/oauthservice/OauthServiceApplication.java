package com.jastao.oauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/*
	@EnableResourceServer - will enable Spring security filter that authenticates request via an incoming oauth2 token.
 */

@SpringBootApplication
@EnableConfigurationProperties
@EnableResourceServer
@EnableDiscoveryClient
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class OauthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthServiceApplication.class, args);
	}

}
