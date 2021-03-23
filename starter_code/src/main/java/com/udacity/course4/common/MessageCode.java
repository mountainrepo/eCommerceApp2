package com.udacity.course4.common;

public enum MessageCode {

    USER_CREATION_SUCCESS(2000, "Successfully created User. "),
    ORDER_CREATION_SUCCESS(2001, "Successfully created Order. "),
    ITEM_ADDITION_SUCCESS(2002, "Successfully added Item. "),
    ITEM_REMOVAL_SUCCESS(2003, "Successfully removed Item");

    private final int code;
    private final String description;

    private MessageCode(int code, String description) {
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
