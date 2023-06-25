package com.iffomko.apsofttesttask;

import com.iffomko.apsofttesttask.controllers.FilesLoaderController;
import com.iffomko.apsofttesttask.services.FilesLoaderService;
import com.iffomko.apsofttesttask.services.parser.IFileParser;
import com.iffomko.apsofttesttask.services.parser.IntoHtmlFileParser;
import com.iffomko.apsofttesttask.services.responses.FilesLoaderErrorResponse;
import com.iffomko.apsofttesttask.services.responses.FilesLoaderResponse;
import com.iffomko.apsofttesttask.services.responses.enums.FileLoaderResponseCodes;
import com.iffomko.apsofttesttask.services.responses.enums.FileLoaderResponseMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FilesLoaderControllerTests {
    @Mock
    private MultipartFile multipartFile;
    private final IFileParser parser = Mockito.mock(IntoHtmlFileParser.class);
    private final FilesLoaderService service = new FilesLoaderService(parser);
    private final FilesLoaderController filesLoaderController = new FilesLoaderController(service);

    @Test
    @DisplayName(
            "POST /api/v1/files/parser тестирует случай, " +
            "когда HTTP статус BAD REQUEST и тип у MultipartFile не совпадает с ожидаемым"
    )
    void handlePostFileParser_returnsBadRequestOfIncorrectRequestType() {
        when(multipartFile.getContentType()).thenReturn(MediaType.MULTIPART_FORM_DATA_VALUE);

        ResponseEntity<?> actualResult = filesLoaderController.handlePostFileParser(multipartFile);
        FilesLoaderErrorResponse body = (FilesLoaderErrorResponse)actualResult.getBody();

        assertEquals(actualResult.getStatusCode(), HttpStatus.BAD_REQUEST);
        assert body != null;
        assertEquals(body.getMessage(), FileLoaderResponseMessages.INCORRECT_REQUEST_TYPE.getMessage());
        assertEquals(body.getCode(), FileLoaderResponseCodes.INCORRECT_REQUEST_TYPE.name());
    }

    @Test
    @DisplayName(
            "POST /api/v1/files/parser тестирует случай, " +
            "когда HTTP статус INTERNAL SERVER ERROR и при попытке получить байты с MultipartFile возникает ошибка"
    )
    void handlePostFileParser_returnsInternalServerError() {
        when(multipartFile.getContentType()).thenReturn(MediaType.TEXT_PLAIN_VALUE);

        try {
            when(multipartFile.getBytes()).thenThrow(new IOException("Failed get bytes"));
        } catch (IOException e) {
            // just ignore
        }

        ResponseEntity<?> actualResult = filesLoaderController.handlePostFileParser(multipartFile);
        FilesLoaderErrorResponse body = (FilesLoaderErrorResponse)actualResult.getBody();

        assertEquals(actualResult.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assert body != null;
        assertEquals(body.getMessage(), FileLoaderResponseMessages.INTERNAL_SERVER_ERROR.getMessage());
        assertEquals(body.getCode(), FileLoaderResponseCodes.INTERNAL_SERVER_ERROR.name());
    }

    @Test
    @DisplayName(
            "POST /api/v1/files/parser тестирует случай, " +
            "когда HTTP статус OK и возвращаемые данные с сервиса совпадают"
    )
    void handlePostFileParser_returnsOk() {
        when(multipartFile.getContentType()).thenReturn(MediaType.TEXT_PLAIN_VALUE);

        try {
            when(multipartFile.getBytes()).thenReturn("test".getBytes());
        } catch (IOException e) {
            // just ignore
        }

        when(parser.parse(anyList())).thenReturn("test");

        ResponseEntity<?> actualResult = filesLoaderController.handlePostFileParser(multipartFile);
        FilesLoaderResponse body = (FilesLoaderResponse) actualResult.getBody();

        assertEquals(actualResult.getStatusCode(), HttpStatus.OK);
        assert body != null;
        assertEquals(body.getCode(), FileLoaderResponseCodes.SUCCESS.name());
        assertEquals(body.getData(), "test");
    }
}
