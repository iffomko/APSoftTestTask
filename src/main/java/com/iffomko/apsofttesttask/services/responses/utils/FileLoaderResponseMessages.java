package com.iffomko.apsofttesttask.services.responses.utils;

/**
 * Перечисление сообщений для возврата в ответе <code>FilesLoaderResponse</code>
 */
public enum FileLoaderResponseMessages {
    INCORRECT_ENCODING(
            "You have sent a file with an incorrect encoding." +
            "Read the documentation to find out what encoding the file should have."
    ),
    INCORRECT_REQUEST_TYPE(
            "You sent an incorrect content-type. " +
            "Read the documentation to understand what type of data you need to send."
    ),
    INTERNAL_SERVER_ERROR(
            "An internal error occurred on the server, " +
            "which is why it cannot process the request correctly. Try again later."
    );
    private final String message;

    FileLoaderResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
