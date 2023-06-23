package com.iffomko.apsofttesttask.services.parser;

/**
 * Закрепляет контракт для всех парсеров файлов, чтобы был метод <code>parse(String file)</code>,
 * который принимает какой-то файл в виде строчки и возвращает обработанные данные в определенном формате
 */
public interface IFileParser {
    /**
     * Метод, который парсит данные из входящего потока
     * @param file файл, содержащий данные для обработки
     * @return обработанные данные
     */
    String parse(String file);
}
