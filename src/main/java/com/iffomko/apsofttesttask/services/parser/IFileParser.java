package com.iffomko.apsofttesttask.services.parser;

import java.io.File;
import java.io.IOException;

/**
 * Закрепляет контракт для всех парсеров файлов, чтобы был метод <code>parse(File file)</code>,
 * который принимает какой-то файл и возвращает обработанные данные в определенном формате
 */
public interface IFileParser {
    /**
     * Метод, который парсит текст файла в определенный формат
     *
     * @param file файл, текст которого нужно обработать
     * @return переформатированный текст файла
     * @throws IOException возникает когда невозможно прочитать файл
     */
    String parse(File file) throws IOException;
}
