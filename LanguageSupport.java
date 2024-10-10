package com.operations.calculator;

import java.util.HashMap;

public class LanguageSupport {
    private static final HashMap<String, String> messages = new HashMap<>();

    static {
        messages.put("en", "Result:");
        messages.put("es", "Resultado:");
        // More languages can be added here
    }

    public String getMessage(String lang) {
        return messages.getOrDefault(lang, messages.get("en"));
    }
}
