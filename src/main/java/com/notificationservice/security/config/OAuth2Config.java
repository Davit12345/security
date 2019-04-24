package com.notificationservice.security.config;


import com.notificationservice.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;


import javax.sql.DataSource;

@Configuration
public class OAuth2Config {


    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


        @Value("${security.oauth2.client.id}")
        private String clientId;

        @Value("${security.oauth2.client.client-secret}")
        private String clientSecret;

        @Value("${security.oauth2.client.access-token-validity-seconds}")
        private int accessTokenValiditySeconds;

        @Value("${security.oauth2.client.authorized-grant-types}")
        private String[] grantTypes;

        @Value("${security.oauth2.client.scope}")
        private String[] scope;


        @Autowired
        @Qualifier("dataSource")
        private DataSource dataSource;



        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Autowired
        private AuthenticationManager authenticationManager;


        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

            endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);

        }


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory().withClient(clientId).scopes(scope)
                    .authorizedGrantTypes(grantTypes).secret(clientSecret)
                    .accessTokenValiditySeconds(accessTokenValiditySeconds);
        }





    }}