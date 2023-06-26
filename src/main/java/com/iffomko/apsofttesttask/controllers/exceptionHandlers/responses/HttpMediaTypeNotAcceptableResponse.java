package com.iffomko.apsofttesttask.controllers.exceptionHandlers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpMediaTypeNotAcceptableResponse {
    private String message;
    private String code;
    private int statusCode;
    private String[] supportedMediaTypes;
}
