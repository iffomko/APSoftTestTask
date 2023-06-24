package com.iffomko.apsofttesttask.controllers.advices;

import com.iffomko.apsofttesttask.controllers.advices.enums.AdviceCodes;
import com.iffomko.apsofttesttask.controllers.advices.enums.AdviceMessages;
import com.iffomko.apsofttesttask.controllers.advices.responses.SizeLimitExceededExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;

@Slf4j
@RestControllerAdvice
public class CustomAdvice {
    @ExceptionHandler(value = SizeLimitExceededException.class)
    public ResponseEntity<SizeLimitExceededExceptionResponse> SizeLimitExceededExceptionHandler(
            SizeLimitExceededException ex,
            WebRequest request
    ) {
        log.debug(MessageFormat.format(
                "{0}, actual size: {1}",
                AdviceMessages.SIZE_LIMIT_EXCEEDED_EXCEPTION.getMessage(),
                ex.getActualSize()
        ));
        return ResponseEntity.badRequest().body(new SizeLimitExceededExceptionResponse(
                AdviceMessages.SIZE_LIMIT_EXCEEDED_EXCEPTION.getMessage(),
                AdviceCodes.SIZE_LIMIT_EXCEEDED_EXCEPTION.name(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getActualSize(),
                ex.getPermittedSize()
        ));
    }
}
