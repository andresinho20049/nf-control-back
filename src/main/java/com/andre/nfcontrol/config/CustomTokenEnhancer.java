package com.andre.nfcontrol.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.andre.nfcontrol.models.User;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    	User user = (User) authentication.getPrincipal();    	
    	
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("id", user.getId());
        additionalInfo.put("name", user.getName()); 
        additionalInfo.put("updatePassword", user.isUpdatePassword());
        
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}