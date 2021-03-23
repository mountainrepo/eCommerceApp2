package com.udacity.course4.common;

public enum ErrorCode {

    // Not found code
    USER_ID_NOT_FOUND(1000, "User Id not found"),
    USER_NAME_NOT_FOUND(1001, "User Name not found"),
    ITEMS_NOT_FOUND(1002, "Items not found"),
    ITEM_ID_NOT_FOUND(1003, "Item Id not found"),
    ITEM_NAME_NOT_FOUND(1004, "Item Name not found"),
    // ------------------------------------------------
    // Validation error code
    PASSWORD_INVALID(1020, "Input Password invalid"),
    CONFIRM_PASSWORD_INVALID(1021, "Input Confirm Password invalid"),
    USER_NAME_NULL(1022, "User Name null"),
    ITEM_NAME_NULL(1023, "Item Name null"),
    ITEM_ID_NULL(1024, "Item Id null"),
    ITEM_ID_ZERO(1025, "Item Id zero"),
    QUANTITY_ZERO(1026, "Quantity zero"),
    // ------------------------------------------------
    // Processing error code
    DATABASE_REPOSITORY_ERROR(1027, "Database Repository processing error");

    private final int code;
    private final String description;

    private ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
