package com.iffomko.apsofttesttask.controllers.exceptionHandlers.utils;

public enum ResponseEntityExceptionHandlerMessages {
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("The method you use for the request is not supported by the server"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED("Unsupported media was received or processed"),
    MISSING_PATH_VARIABLE("You missed a variable in the URI"),
    MISSING_SERVLET_REQUEST_PARAMETER("You missed a parameter in the URI"),
    MISSING_SERVLET_REQUEST_PART("The part of the \"multipart/form-data\" query identified by name could not be found"),
    METHOD_ARGUMENT_NOT_VALID("You specified an invalid parameter"),
    NO_HANDLER_FOUND_EXCEPTION("There is no such endpoint. All the endpoints can be found in the documentation."),
    ASYNC_REQUEST_TIMEOUT_EXCEPTION("Asynс request timeout has expired"),
    SIZE_LIMIT_EXCEEDED_EXCEPTION("You have exceeded the maximum size of the uploaded data");

    private final String message;

    ResponseEntityExceptionHandlerMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
