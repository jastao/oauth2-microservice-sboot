package com.jastao.oauthservice.config.mongoconf;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories( basePackages = "com.jastao.oauthservice.repository")
public class MongoConfig {
}
