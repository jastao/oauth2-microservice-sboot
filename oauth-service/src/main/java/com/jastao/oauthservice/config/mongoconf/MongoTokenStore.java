package com.jastao.oauthservice.config.mongoconf;

import com.jastao.oauthservice.domain.MongoAccessToken;
import com.jastao.oauthservice.domain.MongoRefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MongoTokenStore implements TokenStore {

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    
    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
        
        return readAuthentication(accessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessToken.TOKEN_ID).is(extractTokenKey(token)));

        MongoAccessToken mongoAccessToken = mongoTemplate.findOne(query, MongoAccessToken.class);
        return mongoAccessToken != null ? mongoAccessToken.getAuthentication() : null;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        String refreshToken = null;

        if(accessToken.getRefreshToken() != null) {
            refreshToken = accessToken.getRefreshToken().getValue();
        }

        if(readAccessToken(accessToken.getValue()) != null) {
            removeAccessToken(accessToken);
        }

        MongoAccessToken mongoAccessToken = new MongoAccessToken();
        mongoAccessToken.setTokenId(extractTokenKey(accessToken.getValue()));
        mongoAccessToken.setToken(accessToken);
        mongoAccessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        mongoAccessToken.setAuthentication(authentication);
        mongoAccessToken.setClientId(authentication.isClientOnly() ? null : authentication.getName());
        mongoAccessToken.setUsername(authentication.getOAuth2Request().getClientId());
        mongoAccessToken.setRefreshToken(refreshToken);

        mongoTemplate.save(mongoAccessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessToken.TOKEN_ID).is(extractTokenKey(tokenValue)));

        MongoAccessToken mongoAccessToken = mongoTemplate.findOne(query, MongoAccessToken.class);
        return mongoAccessToken != null ? mongoAccessToken.getToken() : null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessToken.TOKEN_ID).is(extractTokenKey(oAuth2AccessToken.getValue())));

        mongoTemplate.remove(query, MongoAccessToken.class);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {

        MongoRefreshToken mongoRefreshToken = new MongoRefreshToken();
        mongoRefreshToken.setTokenId(extractTokenKey(refreshToken.getValue()));
        mongoRefreshToken.setToken(refreshToken);
        mongoRefreshToken.setAuthentication(authentication);
        mongoTemplate.save(refreshToken);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoRefreshToken.TOKEN_ID).is(extractTokenKey(tokenValue)));

        MongoRefreshToken mongoRefreshToken = mongoTemplate.findOne(query, MongoRefreshToken.class);
        return mongoRefreshToken != null ? mongoRefreshToken.getToken() : null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoRefreshToken.TOKEN_ID).is(extractTokenKey(refreshToken.getValue())));
        MongoRefreshToken mongoRefreshToken = mongoTemplate.findOne(query, MongoRefreshToken.class);
        return mongoRefreshToken != null ? mongoRefreshToken.getAuthentication() : null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoRefreshToken.TOKEN_ID).is(extractTokenKey(refreshToken.getValue())));
        mongoTemplate.remove(query, MongoRefreshToken.class);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessToken.REFRESH_TOKEN).is(extractTokenKey(refreshToken.getValue())));
        mongoTemplate.remove(query, MongoAccessToken.class);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {

        OAuth2AccessToken accessToken = null;

        String authenticatedId = authenticationKeyGenerator.extractKey(authentication);

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoAccessToken.AUTHENTICATION_ID).is(authenticatedId));

        MongoAccessToken mongoAccessToken = mongoTemplate.findOne(query, MongoAccessToken.class);
        if(mongoAccessToken != null) {

            accessToken = mongoAccessToken.getToken();
            if(accessToken != null && !authenticatedId.equals(authenticationKeyGenerator.extractKey(mongoAccessToken.getAuthentication()))) {
                this.removeAccessToken(accessToken);
                this.storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {

        return findTokensByCriteria(
                Criteria.where(MongoAccessToken.CLIENT_ID).is(clientId)
                        .where(MongoAccessToken.USERNAME).is(username));
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return findTokensByCriteria(Criteria.where(MongoAccessToken.CLIENT_ID).is(clientId));
    }

    private Collection<OAuth2AccessToken> findTokensByCriteria(Criteria criteria) {

        Collection<OAuth2AccessToken> tokens = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(criteria);
        List<MongoAccessToken> mongoAccessTokens = mongoTemplate.find(query, MongoAccessToken.class);

        tokens = mongoAccessTokens.stream()
                    .map( item -> item.getToken() )
                    .collect(Collectors.toList());
        return tokens;
    }

    private String extractTokenKey( String token ) {

        if( token == null) {
            return null;
        } else {

            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");

            } catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException("MD5 algorithm is not available.");
            }

            try {
                byte[] key = digest.digest(token.getBytes(StandardCharsets.UTF_8.name()));
                return String.format("%032x", new BigInteger(1, key));

            }catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException("UTF-8 encoding is not available.");
            }
        }
    }
}
