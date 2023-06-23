package com.iffomko.apsofttesttask.services.parser;

import org.springframework.stereotype.Service;

/**
 * <p>Парсер, который обрабатывает текст.</p>
 * <p>Входной файл должен иметь следующий формат:</p>
 * <p>&nbsp;&nbsp;1. В начале каждой подстроки может быть признак начала раздела или подраздела - символ #</p>
 * <p>&nbsp;&nbsp;2. Количество символов "#" в начале строки указывает на уровень вложенности раздела</p>
 * <p>Выходная строчка будет иметь формат html следующего вида:</p>
 * <p>&nbsp;&nbsp;1. Сначала идет блок "Содержание", где отображены все разделы</p>
 * <p>&nbsp;&nbsp;2. И сразу после него весь текст</p>
 */
@Service("fileParser")
public class FileParser implements IFileParser {
    /**
     * Метод, который парсит текст
     *
     * @param file текст файла в подходящем формате
     * @return переформатированный текст файла
     */
    @Override
    public String parse(String file) {
        return file;
    }
}
