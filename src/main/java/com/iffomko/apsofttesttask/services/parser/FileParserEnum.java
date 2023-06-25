package com.iffomko.apsofttesttask.services.parser;

public enum FileParserEnum {
    ILLEGAL_STATE_EXCEPTION("Input list of words is null");

    private final String message;

    FileParserEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
