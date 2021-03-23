package com.udacity.course4.common;

public class ErrorCodes {

    // Not Found error codes
    public static final int USER_ID_NOT_FOUND = 1000;
    public static final int USER_NAME_NOT_FOUND = 1001;
    public static final int ITEMS_NOT_FOUND = 1002;
    public static final int ITEM_ID_NOT_FOUND = 1003;
    public static final int ITEM_NAME_NOT_FOUND = 1004;

    // Validation error codes
    public static final int PASSWORD_INVALID = 1020;
    public static final int CONFIRM_PASSWORD_INVALID = 1021;
    public static final int USER_NAME_NULL = 1022;
    public static final int ITEM_NAME_NULL = 1023;
    public static final int ITEM_ID_NULL = 1024;
    public static final int ITEM_ID_ZERO = 1025;
    public static final int QUANTITY_ZERO = 1026;

    // Processing error code
    public static final int DATABASE_REPOSITORY_ERROR = 1050;
}
