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

/**
 * Обработчик базовых исключений, которые есть в классе <code>ResponseEntityExceptionHandler</code>
 */
@Slf4j
@RestControllerAdvice
public class DefaultAdvice extends ResponseEntityExceptionHandler {
    /**
     * Перехватывает исключение <code>HttpRequestMethodNotSupportedException</code>,
     * которое возникает тогда, когда обработчик запросов не поддерживает метод
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>HttpMediaTypeNotSupportedException</code>,
     * которое возникает тогда, когда тип контента в POSTs, PUTs, PATCHes запросах
     * не поддерживается обработчиком запросов
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>HttpMediaTypeNotAcceptableException</code>,
     * которое возникает когда у клиента заголовок <code>Accept</code>, не поддерживает
     * тот тип, который хочет вернуть сервер
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>MissingPathVariableException</code>,
     * которое возникает когда клиент в URI пропустил необходимую переменную
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>MissingServletRequestParameterException</code>,
     * которое возникает когда клиент пропустил необходимый параметр
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>MissingServletRequestPartException</code>,
     * которое возникает когда сервер не нашел часть 'multipart/form-data' запроса
     * с указанным именем
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>ServletRequestBindingException</code>,
     * которое возникает когда исключения привязки мы хотим рассматривать как
     * неустранимые
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>MethodArgumentNotValidException</code>,
     * которое возникает когда аргумент в методе не валидный
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>NoHandlerFoundException</code>,
     * которое возникает когда не нашлось подходящего обработчика запросов
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>AsyncRequestTimeoutException</code>,
     * которое возникает когда время выполнения асинхронного запроса вышло
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>ConversionNotSupportedException</code>,
     * которое возникает когда не удалось конвертировать данные в конкретный бин
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>TypeMismatchException</code>,
     * которое возникает когда при конвертировании бин тип данных и тип поля не совпали
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>HttpMessageNotReadableException</code>,
     * которое возникает когда не удалось прочитать HTTP запрос
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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

    /**
     * Перехватывает исключение <code>HttpMessageNotWritableException</code>,
     * которое возникает, когда Spring не удается получить свойства возвращаемого объекта
     * @param ex перехваченное исключение
     * @param headers заголовки запроса
     * @param status код ответа
     * @param request сам запрос
     * @return ответ клиенту в виде JSON
     */
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
