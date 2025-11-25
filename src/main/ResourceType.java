package main;

import java.util.Arrays;

public enum ResourceType {
    CHEAT_SHEET("Cheat Sheet"),
    PENCIL("Pencil"),
    COFFEE("Coffee");

    private final String objectName;

    ResourceType(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }

    public static ResourceType fromObjectName(String objectName) {
        if (objectName == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(value -> value.objectName.equalsIgnoreCase(objectName))
                .findFirst()
                .orElse(null);
    }
}

