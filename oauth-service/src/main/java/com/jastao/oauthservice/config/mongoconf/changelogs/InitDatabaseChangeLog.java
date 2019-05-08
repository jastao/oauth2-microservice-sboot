package com.jastao.oauthservice.config.mongoconf.changelogs;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.jastao.oauthservice.domain.AuthClientDetails;
import com.jastao.oauthservice.domain.Authorities;
import com.jastao.oauthservice.domain.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@ChangeLog
public class InitDatabaseChangeLog {

    @ChangeSet(order = "001", id = "insertAuthClientDetails", author = "jastao")
    public void insertAuthClientDetails(MongoTemplate mongoTemplate) {

        AuthClientDetails sampleAuthClientDetails = new AuthClientDetails();
        sampleAuthClientDetails.setClientId("appsecret");
        sampleAuthClientDetails.setClientSecret("WsHMbLzoKddgHzD9fC69RtWDj91VojWM");
        sampleAuthClientDetails.setGrantTypes("refresh_token,password");
        sampleAuthClientDetails.setScopes("READ");
        mongoTemplate.save(sampleAuthClientDetails);
    }

    @ChangeSet(order = "002", id = "insertAuthenticatedUser", author = "jastao")
    public void insertAuthenticatedUser(MongoTemplate mongoTemplate) {

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(Authorities.ROLE_USER);

        User user = new User();
        user.setActivated(true);
        user.setAuthorities(authorities);
        user.setUsername("testuser");
        user.setPassword("WsHMbLzoKddgHzD9fC69RtWDj91VojWM");

        mongoTemplate.save(user);
    }
}
