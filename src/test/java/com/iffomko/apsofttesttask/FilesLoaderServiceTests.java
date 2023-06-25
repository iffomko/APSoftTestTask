package com.iffomko.apsofttesttask;

import com.iffomko.apsofttesttask.services.FilesLoaderService;
import com.iffomko.apsofttesttask.services.parser.IFileParser;
import com.iffomko.apsofttesttask.services.parser.IntoHtmlFileParser;
import com.iffomko.apsofttesttask.services.responses.FilesLoaderErrorResponse;
import com.iffomko.apsofttesttask.services.responses.FilesLoaderResponse;
import com.iffomko.apsofttesttask.services.responses.enums.FileLoaderResponseCodes;
import com.iffomko.apsofttesttask.services.responses.enums.FileLoaderResponseMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class FilesLoaderServiceTests {
    private final IFileParser parser = Mockito.mock(IntoHtmlFileParser.class);
    private final MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
    private final FilesLoaderService service = new FilesLoaderService(parser);

    @Test
    @DisplayName("Тестирование обычного случая")
    void testPositiveCase() {
        String inputText = """
                GREATEST MAN IN ALIVE
                #Chapter one
                this story about awesome dude that call name is Jack
                ##Jack's characteristics""";

        String chapterOneTag = String.format("%d_%d", 1, Math.abs("#Chapter one".hashCode()));
        String jacksTag = String.format("%d_%d", 3, Math.abs("##Jack's characteristics".hashCode()));

        String outputText = String.format("""
                            <!DOCTYPE html>
                            <html lang="en">
                            <head>
                                <meta charset="UTF-8">
                                <title>Title</title>
                                <link rel="preconnect" href="https://fonts.googleapis.com">
                                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                                <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
                                <style>
                                    * {
                                        font-family: 'Roboto', sans-serif;
                                        color: #333;
                                        font-size: 15px;
                                        font-weight: 400;
                                    }
                                    a {
                                        font-family: 'Roboto', sans-serif;
                                        color: #333;
                                        font-size: 15px;
                                        font-style: normal;
                                        font-weight: 400;
                                        text-decoration: none;
                                    }
                                    a:visited, a:focus, a:hover {
                                        color: #333;
                                    }
                                    a.section_link {
                                        font-style: italic;
                                    }
                                    a.section_link:hover {
                                        text-decoration: underline;
                                    }
                                    h1 {
                                        font-family: 'Roboto', sans-serif;
                                        color: #333;
                                        font-size: 22px;
                                        font-weight: 400;
                                                        
                                        margin: 10px 0;
                                    }
                                </style>
                            </head>
                            <body>
                            <h1>Содержание:</h1>
                            <div><a class="section_link" href="#%s">- Chapter one</a></div><div><a class="section_link" href="#%s">-- Jack's characteristics</a></div>
                            <h1>Текст:</h1>
                            <div>GREATEST MAN IN ALIVE</div>
                            <div><a name="%s">Chapter one</a></div>
                            <div>this story about awesome dude that call name is Jack</div>
                            <div><a name="%s">Jack's characteristics</a></div>
                            </body>
                            </html>
                            """,
                chapterOneTag,
                jacksTag,
                chapterOneTag,
                jacksTag
        );

        when(multipartFile.getContentType()).thenReturn(MediaType.TEXT_PLAIN_VALUE);

        try {
            when(multipartFile.getBytes()).thenReturn(inputText.getBytes());
        } catch (IOException e) {
            // just ignore
        }

        when(parser.parse(anyList())).thenReturn(outputText);

        ResponseEntity<?> actualResult = service.parseFile(multipartFile);

        ResponseEntity<FilesLoaderResponse> expectedResult = ResponseEntity.ok(new FilesLoaderResponse(
                FileLoaderResponseCodes.SUCCESS.name(),
                outputText
        ));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Тестирование несовпадения типа загружаемого ресурса")
    void testForNotEqualsMediaTypeOfFile() {
        ResponseEntity<?> expectedResult = ResponseEntity.badRequest().body(new FilesLoaderErrorResponse(
                FileLoaderResponseCodes.INCORRECT_REQUEST_TYPE.name(),
                FileLoaderResponseMessages.INCORRECT_REQUEST_TYPE.getMessage()
        ));

        when(multipartFile.getContentType()).thenReturn(MediaType.MULTIPART_FORM_DATA_VALUE);

        ResponseEntity<?> actualResult = service.parseFile(multipartFile);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Тестирование непредвиденного исключения")
    void testForInternalServerError() {
        ResponseEntity<?> expectedResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FilesLoaderErrorResponse(
                FileLoaderResponseMessages.INTERNAL_SERVER_ERROR.getMessage(),
                FileLoaderResponseCodes.INTERNAL_SERVER_ERROR.name()
        ));

        try {
            when(multipartFile.getContentType()).thenReturn(MediaType.TEXT_PLAIN_VALUE);
            when(multipartFile.getBytes()).thenThrow(new IOException("Failed to get bytes"));
        } catch (IOException e) {
            // just ignore
        }

        ResponseEntity<?> actualResult = service.parseFile(multipartFile);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Тестирование когда файл равен null")
    void testForInputMultipartFileIsNull() {
        ResponseEntity<?> expectedResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FilesLoaderErrorResponse(
                FileLoaderResponseMessages.INTERNAL_SERVER_ERROR.getMessage(),
                FileLoaderResponseCodes.INTERNAL_SERVER_ERROR.name()
        ));

        ResponseEntity<?> actualResult = service.parseFile(null);

        assertEquals(expectedResult, actualResult);
    }
}
