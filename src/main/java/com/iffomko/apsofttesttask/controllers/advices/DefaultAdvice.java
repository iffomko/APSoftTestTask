package com.iffomko.apsofttesttask.controllers.advices;

import com.iffomko.apsofttesttask.controllers.advices.enums.AdviceCodes;
import com.iffomko.apsofttesttask.controllers.advices.enums.AdviceMessages;
import com.iffomko.apsofttesttask.controllers.advices.responses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class DefaultAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new HttpRequestMethodNotSupportedResponse(
                AdviceMessages.HTTP_REQUEST_METHOD_NOT_SUPPORTED.getMessage(),
                AdviceCodes.HTTP_REQUEST_METHOD_NOT_SUPPORTED.name(),
                status.value(),
                ex.getMethod(),
                ex.getSupportedMethods()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new HttpMediaTypeNotSupportedResponse(
                AdviceMessages.HTTP_MEDIA_TYPE_NOT_SUPPORTED.getMessage(),
                AdviceCodes.HTTP_MEDIA_TYPE_NOT_SUPPORTED.name(),
                status.value(),
                Objects.requireNonNull(ex.getContentType()).getType(),
                (String[]) ex.getSupportedMediaTypes().stream().map(MediaType::getType).toArray()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new HttpMediaTypeNotAcceptableResponse(
                ex.getMessage(),
                AdviceCodes.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE.name(),
                status.value(),
                (String[]) ex.getSupportedMediaTypes().stream().map(MediaType::getType).toArray()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(MessageFormat.format("{0}, variable name: {1}", ex.getMessage(), ex.getVariableName()));
        return ResponseEntity.status(status.value()).body(new MissingPathVariableResponse(
                AdviceMessages.MISSING_PATH_VARIABLE.getMessage(),
                AdviceCodes.MISSING_PATH_VARIABLE.name(),
                status.value(),
                ex.getVariableName()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(MessageFormat.format(
                "{0}, type: {1}, name: {2}",
                ex.getMessage(),
                ex.getParameterType(),
                ex.getParameterName()
        ));
        return ResponseEntity.status(status.value()).body(new MissingServletRequestParameterResponse(
                AdviceMessages.MISSING_SERVLET_REQUEST_PARAMETER.getMessage(),
                AdviceCodes.MISSING_SERVLET_REQUEST_PARAMETER.name(),
                status.value(),
                ex.getParameterType(),
                ex.getParameterName()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(MessageFormat.format(
                "{0}, part-name: {1}",
                ex.getMessage(),
                ex.getRequestPartName()
        ));
        return ResponseEntity.status(status.value()).body(new MissingServletRequestPartResponse(
                AdviceMessages.MISSING_SERVLET_REQUEST_PART.getMessage(),
                AdviceCodes.MISSING_SERVLET_REQUEST_PART.name(),
                status.value(),
                ex.getRequestPartName()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new BaseResponse(
                ex.getMessage(),
                AdviceCodes.MISSING_SERVLET_BINDING_EXCEPTION.name(),
                status.value()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(MessageFormat.format(
                "{0}, parameter-name: {1}",
                ex.getMessage(),
                ex.getParameter().getParameterName()
        ));
        return ResponseEntity.status(status.value()).body(new MethodArgumentNotValidResponse(
                AdviceMessages.METHOD_ARGUMENT_NOT_VALID.getMessage(),
                AdviceCodes.METHOD_ARGUMENT_NOT_VALID.name(),
                status.value(),
                ex.getParameter().getParameterName()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(MessageFormat.format(
                "{0}, parameter-name: {1}",
                ex.getMessage(),
                ex.getRequestURL()
        ));
        return ResponseEntity.status(status.value()).body(new NoHandlerFoundExceptionResponse(
                AdviceMessages.NO_HANDLER_FOUND_EXCEPTION.getMessage(),
                AdviceCodes.NO_HANDLER_FOUND_EXCEPTION.name(),
                status.value(),
                ex.getRequestURL()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            AsyncRequestTimeoutException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new BaseResponse(
                AdviceMessages.ASYNC_REQUEST_TIMEOUT_EXCEPTION.getMessage(),
                AdviceCodes.ASYNC_REQUEST_TIMEOUT_EXCEPTION.name(),
                status.value()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(
            ErrorResponseException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new BaseResponse(
                ex.getMessage(),
                AdviceCodes.ERROR_RESPONSE_EXCEPTION.name(),
                status.value()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new BaseResponse(
                ex.getMessage(),
                AdviceCodes.CONVERSION_NOT_SUPPORTED.name(),
                status.value()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new BaseResponse(
                ex.getMessage(),
                AdviceCodes.TYPE_MISMATCH.name(),
                status.value()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new BaseResponse(
                ex.getMessage(),
                AdviceCodes.HTTP_MESSAGE_NOT_READABLE.name(),
                status.value()
        ));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(status.value()).body(new BaseResponse(
                ex.getMessage(),
                AdviceCodes.HTTP_MESSAGE_NOT_WRITABLE.name(),
                status.value()
        ));
    }
}
