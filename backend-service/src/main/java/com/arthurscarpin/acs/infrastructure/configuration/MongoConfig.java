package com.arthurscarpin.acs.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.arthurscarpin.acs.infrastructure.persistence.repository.document")
public class MongoConfig {
}
