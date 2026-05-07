package com.arthurscarpin.acs.infrastructure.configuration;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.arthurscarpin.acs.infrastructure.persistence.repository.table")
@EntityScan(basePackages = "com.arthurscarpin.acs.infrastructure.persistence.entity.table")
public class JpaConfig {
}
