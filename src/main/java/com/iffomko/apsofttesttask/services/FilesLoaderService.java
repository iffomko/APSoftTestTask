package com.iffomko.apsofttesttask.services;

import com.iffomko.apsofttesttask.services.responses.FilesLoaderErrorResponse;
import com.iffomko.apsofttesttask.services.responses.FilesLoaderResponse;
import com.iffomko.apsofttesttask.services.responses.enums.FileLoaderResponseCodes;
import com.iffomko.apsofttesttask.services.parser.IFileParser;
import com.iffomko.apsofttesttask.services.responses.enums.FileLoaderResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Сервис с бизнес-логикой по обработке загружаемых файлов.
 */
@Service
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
            String fileText = new String(multipartFile.getBytes());
            String resultText = this.fileParser.parse(fileText);

            return ResponseEntity.ok(new FilesLoaderResponse(
                    FileLoaderResponseCodes.SUCCESS.name(),
                    resultText
            ));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new FilesLoaderErrorResponse(
                    FileLoaderResponseCodes.INCORRECT_ENCODING_OR_FILE.name(),
                    FileLoaderResponseMessages.INCORRECT_ENCODING_OR_FILE.getMessage()
            ));
        }
    }
}
