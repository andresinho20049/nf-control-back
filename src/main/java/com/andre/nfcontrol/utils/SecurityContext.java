package com.andre.nfcontrol.utils;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.andre.nfcontrol.models.User;

public class SecurityContext {

	@Value("${security.client-id}")
	private String clientId;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private ConsumerTokenServices consumerTokenServices;

	public static User getUserLogged() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public Integer revokeTokenByUsername(String username) {

		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, username);
		for (OAuth2AccessToken token : tokens) {
			consumerTokenServices.revokeToken(token.getValue());
		}

		return tokens != null ? tokens.size() : 0;
	}

}
