package com.iffomko.apsofttesttask.services.responses.enums;

/**
 * Перечисление сообщений для возврата в ответе <code>FilesLoaderResponse</code>
 */
public enum FileLoaderResponseMessages {
    INCORRECT_ENCODING_OR_FILE(
            "You have sent a file with an incorrect encoding or file." +
            "Read the documentation to find out what encoding the file should have " +
            "or what should be the file format"
    );
    private final String message;

    FileLoaderResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
