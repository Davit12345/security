package com.notificationservice.security.config;

import com.notificationservice.security.model.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTokenEnhancer extends JwtAccessTokenConverter {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication){

        User user =(User)authentication.getPrincipal();
        Map<String,Object> info= new LinkedHashMap<>(accessToken.getAdditionalInformation());
    if(user.getId()>0){
        info.put("id",user.getId());
    }
        if(user.getName()!=null){
            info.put("name",user.getName());
        }
        if(user.getSurname()!=null){
            info.put("surname",user.getSurname());
        }  if(user.getEmail()!=null){
            info.put("email",user.getEmail());
        }
        DefaultOAuth2AccessToken customAccessToken= new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(info);
        return  super.enhance(customAccessToken,authentication);
        }




    }



