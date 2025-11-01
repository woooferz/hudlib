package dev.wooferz.hudlib.config;

import java.util.HashMap;

public class Config {
    private final HashMap<String, ConfigElementInformation> elements = new HashMap<>();

    public Config() {
    }

    public void saveElement(String identifier, ConfigElementInformation information) {
        elements.put(identifier, information);
    }
    public ConfigElementInformation getElement(String identifier) {
        return elements.get(identifier);
    }
}
