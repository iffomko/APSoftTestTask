package com.iffomko.apsofttesttask.services.parser;

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
    private record Section(String data, String id) {
        public String parse() {
            return MessageFormat.format(
                    "<div><a href=\"#{0}\">{1}</a></div>",
                    id, data);
        }
    }

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

    private record SectionItem(int index, String sectionTitle) {
    }

    private static String removeSectionDesignator(String line) {
        if (!line.startsWith("#")) {
            return line;
        }

        int index = 0;

        for (; (index < line.length()) && (line.charAt(index) == '#'); index++) {
            continue;
        }

        return line.substring(index);
    }

    private static int getDepth(String section) {
        if (!section.startsWith("#")) {
            return 0;
        }

        int index = 0;

        for (; (index < section.length()) && (section.charAt(index) == '#'); index++) {
            continue;
        }

        return index;
    }

    private static String printDepth(int depth) {
        return "-".repeat(Math.max(0, depth - 1));
    }

    /**
     * Метод, который парсит текст файла в определенный формат
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
                lines.add(removeSectionDesignator(line));

                if (line.startsWith("#")) {
                    notParsedSections.add(new SectionItem(index, line));
                }

                index++;
            }

            List<Section> parsedSections = new ArrayList<>();

            for (SectionItem section : notParsedSections) {
                String sectionTitle = section.sectionTitle();
                int sectionIndex = section.index();

                int currentDepth = getDepth(sectionTitle);

                String sectionName = removeSectionDesignator(sectionTitle);
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
