package com.filechooser;

/**
 * Created by Alex Gasparyan on 11/5/2017.
 */

public class Error {

    public static final int NULL_PATH_ERROR = 1;
    public static final int NULL_URI_ERROR = 2;
    public static final int NO_ACTIVITY_ERROR = 3;

    private int type;
    private String message;

    public Error(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
