package com.iffomko.apsofttesttask.services;

import com.iffomko.apsofttesttask.services.responses.FilesLoaderErrorResponse;
import com.iffomko.apsofttesttask.services.responses.FilesLoaderResponse;
import com.iffomko.apsofttesttask.services.responses.enums.FileLoaderResponseCodes;
import com.iffomko.apsofttesttask.services.parser.IFileParser;
import com.iffomko.apsofttesttask.services.responses.enums.FileLoaderResponseMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

/**
 * Сервис с бизнес-логикой по обработке загружаемых файлов.
 */
@Service
@Slf4j
public class FilesLoaderService {
    private final IFileParser fileParser;

    /**
     * @param fileParser парсер файлов
     */
    @Autowired
    public FilesLoaderService(@Qualifier("fileParser") IFileParser fileParser) {
        this.fileParser = fileParser;
    }

    /**
     * <p>
     *     Парсит текстовый файл из формата, где символ '#' указывает на начало раздела,
     *     а количество повторений этого символа для раздела указывает на его вложенность,
     *     в формат HTML, где сначала идет структура разделов, посредством которой
     *     можно осуществлять навигацию по разделам, а затем сам текст.
     * </p>
     * @param multipartFile файл полученный из сети
     */
    public ResponseEntity<?> parseFile(MultipartFile multipartFile) {
        try {
            if (!(Objects.equals(multipartFile.getContentType(), MediaType.TEXT_PLAIN_VALUE))) {
                log.error(MessageFormat.format(
                        "Invalid content-type in the request: {0}",
                        multipartFile.getContentType()
                ));
                return ResponseEntity.badRequest().body(new FilesLoaderErrorResponse(
                        FileLoaderResponseCodes.INCORRECT_REQUEST_TYPE.name(),
                        FileLoaderResponseMessages.INCORRECT_REQUEST_TYPE.getMessage()
                ));
            }

            String filename = UUID.randomUUID().toString();
            String fileExtension = ".txt";

            File tempDir = new File("./temp/");
            tempDir.deleteOnExit();

            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            File tempFile = File.createTempFile(filename, fileExtension, tempDir);
            tempFile.deleteOnExit();

            try (BufferedOutputStream tempFileOutput = new BufferedOutputStream(new FileOutputStream(tempFile))) {
                tempFileOutput.write(multipartFile.getBytes());
            }

            try {
                String resultText = this.fileParser.parse(tempFile);

                return ResponseEntity.ok(new FilesLoaderResponse(
                        FileLoaderResponseCodes.SUCCESS.name(),
                        resultText
                ));
            } finally {
                if (!tempFile.delete()) {
                    log.error(MessageFormat.format(
                            "Failed delete temporary file with path: {0}",
                            tempFile.getAbsolutePath()
                    ));
                }

                if (tempDir.isDirectory() && Objects.requireNonNull(tempDir.listFiles()).length == 0 && !tempDir.delete()) {
                    log.error(MessageFormat.format(
                            "Failed delete temporary directory with path: {0}",
                            tempDir.getAbsolutePath()
                    ));
                }
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("Failed to create a temporary file: {0}", e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FilesLoaderErrorResponse(
                    FileLoaderResponseMessages.INTERNAL_SERVER_ERROR.getMessage(),
                    FileLoaderResponseCodes.INTERNAL_SERVER_ERROR.name()
            ));
        } catch (Exception e) {
            log.error(MessageFormat.format("Error: {0}", e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FilesLoaderErrorResponse(
                    FileLoaderResponseMessages.INTERNAL_SERVER_ERROR.getMessage(),
                    FileLoaderResponseCodes.INTERNAL_SERVER_ERROR.name()
            ));
        }
    }
}
