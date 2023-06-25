package com.iffomko.apsofttesttask.services.parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

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
     * Разделы, которые ссылаются на определенную позицию в тексте
     * @param data содержание разделов
     * @param id идентификатор раздела, на который надо ссылаться в исходном тексте
     */
    private record Section(String data, String id) {
        public String parse() {
            return MessageFormat.format(
                    "<div><a href=\"#{0}\">{1}</a></div>",
                    id, data);
        }
    }

    /**
     * Запись, которая содержит в себе шаблон HTML страницы.
     * В неё вкладываются разделы и сам текст
     * @param sections разделы, которые ссылаются на определенную позицию в тексте
     * @param text исходный текст
     */
    private record Html(List<Section> sections, String text) {
        public String parse() {
            String sectionsText = sections
                    .stream()
                    .map(Section::parse)
                    .collect(Collectors.joining(""));

            return MessageFormat.format(
                    "<!doctype html>" +
                            "<html lang=\"en\">" +
                            "<head>" +
                            "<meta charset=\"UTF-16\">" +
                            "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">" +
                            "<meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">" +
                            "<title>Document</title>" +
                            "</head>" +
                            "<body>" +
                            "<h1>Содержание:</h1>" +
                            "{0}" +
                            "<h1>Текст:</h1>" +
                            "{1}" +
                            "</body>" +
                            "</html>",
                    sectionsText,
                    text
            );
        }
    }

    /**
     * Параграф для одного текста.
     * Это обычный HTML-тег <code>div</code>, в который
     * вкладываются данные. Если <code>id</code> не null,
     * то внутрь <code>div</code> вкладывается ссылка, у которой
     * имя равна этому id
     * @param data данные, которые нужно вложить
     * @param id имя для ссылки
     */
    private record Paragraph(String data, String id) {
        public String parse() {
            if (id == null) {
                return MessageFormat.format(
                        "<div>{0}</div>",
                        data
                );
            }

            return MessageFormat.format(
                    "<div><a name=\"{0}\">{1}</a></div>",
                    id,
                    data
            );
        }
    }

    /**
     * Запись для элемента в списке разделов
     * @param index строка в исходном тексте
     * @param sectionTitle название раздела
     */
    private record SectionItem(int index, String sectionTitle) {
    }

    private final Character sectionTag;

    /**
     * @param sectionTag определяющий признак раздела
     */
    public FileParser(@Value("${section.tag}") Character sectionTag) {
        this.sectionTag = sectionTag;
    }

    /**
     * Удаляет определяющий признак раздела, т. е. он удалит
     * все подряд идущие с начала строки признаки раздела.
     * @param line строка, в которой надо его удалить
     * @param sectionTag определяющий признак раздела
     * @return строка без этого признака
     */
    private static String removeSectionDesignator(String line, Character sectionTag) {
        if (!line.startsWith(sectionTag.toString())) {
            return line;
        }

        int index = 0;

        for (; (index < line.length()) && (line.charAt(index) == sectionTag); index++) {
            continue;
        }

        return line.substring(index);
    }

    /**
     * Определяет вложенность размера.
     * Все зависит от количества символа '#' в начале строки.
     * @param section сам раздел
     * @param sectionTag определяющий признак раздела
     * @return возвращает 0, если это не раздел, или число, которое соответствует вложенности раздела
     */
    private static int getDepth(String section, Character sectionTag) {
        if (!section.startsWith(sectionTag.toString())) {
            return 0;
        }

        int depth = 0;

        for (; (depth < section.length()) && (section.charAt(depth) == sectionTag); depth++) {
            continue;
        }

        return depth;
    }

    /**
     * Возвращает строку соответствующую вложенности раздела.
     * @param depth вложенность раздела
     * @return строка, которую можно поставить в начало каждого раздела
     */
    private static String printDepth(int depth) {
        return "-".repeat(Math.max(0, depth - 1));
    }

    /**
     * Метод, который парсит текст файла в определенный формат.
     *
     * @param file файл, текст которого нужно обработать
     * @return переформатированный текст файла
     * @throws IOException возникает когда невозможно прочитать файл
     */
    @Override
    public String parse(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File not found");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> splitFile = reader.lines().collect(Collectors.toCollection(ArrayList::new));

            List<String> lines = new ArrayList<>();
            List<SectionItem> notParsedSections = new ArrayList<>();

            int index = 0;

            for (String line : splitFile) {
                lines.add(removeSectionDesignator(line, sectionTag));

                if (line.startsWith("#")) {
                    notParsedSections.add(new SectionItem(index, line));
                }

                index++;
            }

            List<Section> parsedSections = new ArrayList<>();

            for (SectionItem section : notParsedSections) {
                String sectionTitle = section.sectionTitle();
                int sectionIndex = section.index();

                int currentDepth = getDepth(sectionTitle, sectionTag);

                String sectionName = removeSectionDesignator(sectionTitle, sectionTag);
                String depth = printDepth(currentDepth);
                String sectionId = UUID.randomUUID().toString();

                parsedSections.add(new Section(depth + sectionName, sectionId));

                lines.add(sectionIndex, new Paragraph(lines.get(sectionIndex), sectionId).parse());
                lines.remove(sectionIndex + 1);
            }

            String text = lines.stream().map(item -> {
                if (item.startsWith("<div")) {
                    return item;
                }

                return new Paragraph(item, null).parse();
            }).collect(Collectors.joining(""));

            return new Html(parsedSections, text).parse();
        }
    }
}
