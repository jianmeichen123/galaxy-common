package com.galaxyinternet.framework.core.utils;

import java.util.Hashtable;

public class FlavorsUtils {
    private static Hashtable<String, String> managers = new Hashtable<String, String>();

    public static Hashtable getManagers() {
    	managers.put("2c2g","d83a7bd3-f277-43cb-883b-03763b39b523");
    	managers.put("2c4g","28451bde-2256-4de0-a959-452895ca94bb");
    	managers.put("2c8g","fa4b87d8-caf3-4b87-a7ac-456c840ab8b9");
    	managers.put("2c16g","07dc3f96-207b-474b-8c71-04d58a63eb9a");
    	managers.put("4c4g","19eb7b0f-5f37-4743-b94e-c86c6847ef4a");
        managers.put("4c8g","0cf46e9a-270f-47ad-9b33-7d5414a57bbe");
        managers.put("4c16g","ab27c615-8ee1-4a43-8aee-3a169c8695db");
        managers.put("8c8g","5d3f9f9d-c205-4d0c-98fa-701f60a163e2");
        managers.put("8c16g","d90ee324-c675-46bf-97bd-2f43811478a0");
        return managers;
    }
}
