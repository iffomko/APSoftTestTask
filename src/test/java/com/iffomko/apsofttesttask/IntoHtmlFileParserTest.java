package com.iffomko.apsofttesttask;

import com.iffomko.apsofttesttask.services.parser.FileParserEnum;
import com.iffomko.apsofttesttask.services.parser.IFileParser;
import com.iffomko.apsofttesttask.services.parser.IntoHtmlFileParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class IntoHtmlFileParserTest {
    private final IFileParser fileParser;

    public IntoHtmlFileParserTest() {
        this.fileParser = new IntoHtmlFileParser('#');
    }

    @Test
    void testTextWithSections() {
        String text = """
                GREATEST MAN IN ALIVE
                #Chapter one
                this story about awesome dude that call name is Jack
                ##Jack's characteristics""";

        String chapterOneTag = String.format("%d_%d", 1, Math.abs("#Chapter one".hashCode()));
        String jacksTag = String.format("%d_%d", 3, Math.abs("##Jack's characteristics".hashCode()));

        String expectedResult = String.format("""
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

        String actualResult = fileParser.parse(new ArrayList<>(List.of(text.split("\n"))));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testTextWithoutSections() {
        String text = """
                GREATEST MAN IN ALIVE
                Chapter one
                this story about awesome dude that call name is Jack
                Jack's characteristics
                height: 71 inch
                weight: 190 pounds
                Chapter two
                Jack was most famous man in alive
                his fame was greater than his popularity
                Jack's patents
                mosquito net
                x-ray
                internal combustion engine""";

        String actualResult = fileParser.parse(new ArrayList<>(List.of(text.split("\n"))));

        String expectedResult = """
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
                            
                            
                            <h1>Текст:</h1>
                            <div>GREATEST MAN IN ALIVE</div>
                            <div>Chapter one</div>
                            <div>this story about awesome dude that call name is Jack</div>
                            <div>Jack's characteristics</div>
                            <div>height: 71 inch</div>
                            <div>weight: 190 pounds</div>
                            <div>Chapter two</div>
                            <div>Jack was most famous man in alive</div>
                            <div>his fame was greater than his popularity</div>
                            <div>Jack's patents</div>
                            <div>mosquito net</div>
                            <div>x-ray</div>
                            <div>internal combustion engine</div>
                            </body>
                            </html>
                            """;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testInputLinesIsNull() {
        try {
            fileParser.parse(null);
        } catch (IllegalArgumentException ex) {
            assertEquals(FileParserEnum.ILLEGAL_STATE_EXCEPTION.getMessage(), ex.getMessage());
        }
    }
}
