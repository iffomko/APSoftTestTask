package com.iffomko.apsofttesttask.controllers.advices.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpMediaTypeNotSupportedResponse {
    private String message;
    private String code;
    private int statusCode;
    private String notSupportedMedia;
    private String[] supportedMediaTypes;
}
