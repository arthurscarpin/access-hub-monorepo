package com.arthurscarpin.acs.infrastructure.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDExtractor {
    private static final Pattern UUID_PATTERN =
            Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    public static String extract(String filePath) {
        Matcher matcher = UUID_PATTERN.matcher(filePath);
        if (matcher.find()) {
            return matcher.group();
        }
        return java.util.UUID.randomUUID().toString();
    }
}