package com.arthurscarpin.acs.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "storage")
@Setter
@Getter
public class StorageConfigProperties {
    private String root = "storage";
    private String backup = "storage/backup";
    private String error = "storage/error";
    private String tmp = "storage/tmp";
    private int maxFiles = 10;
}
