package main;

import java.util.Arrays;

/**
 * Enumeration representing different types of resources in the game.
 * Each resource has an object name and a corresponding database column name
 * for quantity tracking.
 */
public enum ResourceType {
    /** Represents a "Cheat Sheet" resource, tracked in 'cheat_sheet_qty' database column. */
    CHEAT_SHEET("Cheat Sheet", "cheat_sheet_qty"),
    /** Represents a "Pencil" resource, tracked in 'pencil_qty' database column. */
    PENCIL("Pencil", "pencil_qty"),
    /** Represents a "Coffee" resource, tracked in 'coffee_qty' database column. */
    COFFEE("Coffee", "coffee_qty");

    private final String objectName;
    private final String dbColumnName;

    /**
     * Constructs a ResourceType enum constant.
     *
     * @param objectName   The user-friendly name of the object.
     * @param dbColumnName The corresponding database column name for this resource's quantity.
     */
    ResourceType(String objectName, String dbColumnName) {
        this.objectName = objectName;
        this.dbColumnName = dbColumnName;
    }

    /**
     * Returns the user-friendly name of the resource object.
     *
     * @return The object name.
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Returns the database column name associated with this resource's quantity.
     *
     * @return The database column name.
     */
    public String getDbColumnName() {
        return dbColumnName;
    }

    /**
     * Converts a string object name into its corresponding ResourceType enum.
     * The comparison is case-insensitive.
     *
     * @param objectName The string name of the object to convert.
     * @return The matching ResourceType, or null if no match is found.
     */
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