package com.arthurscarpin.acs.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "capture.storage")
@Setter
@Getter
public class StorageConfigProperties {
    private static String root = "storage";
    private static String backup = "storage/backup";
    private static String error = "storage/error";
    private static String tmp = "storage/tmp";
    private static int maxFiles = 10;
}
