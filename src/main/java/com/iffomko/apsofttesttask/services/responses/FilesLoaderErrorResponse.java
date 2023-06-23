package com.iffomko.apsofttesttask.services.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilesLoaderErrorResponse {
    private String message;
    private String code;
}
