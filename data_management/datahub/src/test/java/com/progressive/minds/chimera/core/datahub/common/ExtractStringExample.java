package com.progressive.minds.chimera.core.datahub.common;


import java.util.regex.*;

public class ExtractStringExample {
    public static void main(String[] args) {
        String input = "urn:li:dataFlow:(spark,Chimera Sample Data Pipeline,DEV)";
        String pattern = "\\(([^,]+),([^,]+),([^,]+)\\)";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);

        if (matcher.find()) {
            String extracted = matcher.group(2);
            System.out.println("Extracted String: " + extracted);
        } else {
            System.out.println("No match found.");
        }
    }
}
