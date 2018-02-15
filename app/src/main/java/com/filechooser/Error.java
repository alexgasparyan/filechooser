package com.filechooser;

/**
 * Created by Alex Gasparyan on 11/5/2017.
 */

public class Error {

    private String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
