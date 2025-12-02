package main;

import java.util.Arrays;

public enum ResourceType {
    CHEAT_SHEET("Cheat Sheet", "cheat_sheet_qty"),
    PENCIL("Pencil", "pencil_qty"),
    COFFEE("Coffee", "coffee_qty");

    private final String objectName;
    private final String dbColumnName;

    ResourceType(String objectName, String dbColumnName) {
        this.objectName = objectName;
        this.dbColumnName = dbColumnName;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getDbColumnName() {
        return dbColumnName;
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

