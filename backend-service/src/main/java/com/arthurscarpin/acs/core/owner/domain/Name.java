package com.arthurscarpin.acs.core.owner.domain;

import com.arthurscarpin.acs.core.owner.exception.NameInvalidException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Name(String name) {

    private static final Pattern PATTERN = Pattern.compile("(^|\\s)(\\p{L})");

    public Name {
        if (name == null || name.isBlank()) {
            throw new NameInvalidException("Name cannot be null or blank");
        }
        name = normalize(name);
    }

    private static String normalize(String name) {
        name = name.trim()
                .replaceAll("\\s+", " ")
                .toLowerCase();

        Matcher matcher = PATTERN.matcher(name);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group(1) + matcher.group(2).toUpperCase());
        }

        matcher.appendTail(result);
        return result.toString();
    }
}