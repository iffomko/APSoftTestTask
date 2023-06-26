package com.iffomko.apsofttesttask.controllers.exceptionHandlers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MissingServletRequestPartResponse {
    private String message;
    private String code;
    private int statusCode;
    private String requestPartName;
}
