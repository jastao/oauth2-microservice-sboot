package com.jastao.oauthservice.config.mongoconf;

import com.github.mongobee.Mongobee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@DependsOn("mongoTemplate")
public class MongoBeeConfig {

    private static final String MONGODB_URL_FORMAT = "mongodb://%s:%s@%s:%d/%s";
    private static final String MONGODB_CHANGLOGS_PACKAGE = "com.jastao.oauthservice.config.mongoconf.changelogs";

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Mongobee mongoBee() {
        Mongobee runner = new Mongobee( String.format(MONGODB_URL_FORMAT,
                    mongoProperties.getUsername(),
                    mongoProperties.getPassword(),
                    mongoProperties.getHost(),
                    mongoProperties.getPort(),
                    mongoProperties.getDatabase()));
            runner.setMongoTemplate(mongoTemplate);
            runner.setDbName(mongoProperties.getDatabase());
            runner.setChangeLogsScanPackage(MONGODB_CHANGLOGS_PACKAGE);

        return runner;
    }
}
