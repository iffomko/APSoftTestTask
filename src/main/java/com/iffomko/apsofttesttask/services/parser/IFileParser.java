package com.iffomko.apsofttesttask.services.parser;

import java.util.List;

/**
 * Закрепляет контракт для всех парсеров строчек, чтобы был метод <code>parse(List<String> lines)</code>,
 * который принимает какой-то набор строчек и возвращает обработанные данные в определенном формате
 */
public interface IFileParser {
    /**
     * Метод, который парсит текст файла в определенный формат
     *
     * @param splitText список строчек, которые надо обработать
     * @return переформатированный текст
     */
    String parse(List<String> splitText);
}
