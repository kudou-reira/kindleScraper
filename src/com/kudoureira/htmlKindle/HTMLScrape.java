package com.kudoureira.htmlKindle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.kudoureira.crawlKindle.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLScrape {
    private File[] files;
    private ArrayList<Book> books = new ArrayList<>();

    public HTMLScrape(File[] files) {
        this.files = files;
    }

    public void scan() {
        Set<Character.UnicodeBlock> japaneseUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {{
            add(Character.UnicodeBlock.HIRAGANA);
            add(Character.UnicodeBlock.KATAKANA);
            add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
        }};

        for(File filepath : files) {
            System.out.println("file path" + filepath);
            File in = new File(filepath.toString());
            try {
                Document doc = Jsoup.parse(in, null);

                Elements wordDivs = doc.select("div");

                String bookTitle = "";
                String author = "";
                HashSet<String> words = new HashSet<>();

                for(Element wordDiv : wordDivs) {
                    int wordLength = wordDiv.text().length();
                    int tempLength = 0;

                    if (wordDiv.text().equalsIgnoreCase("Notebook Export")) {
                        bookTitle = wordDiv.nextElementSibling().text();
                        author = wordDiv.nextElementSibling().nextElementSibling().text();
                        System.out.println("this is the book title " + bookTitle);
                        System.out.println("this is the author " + author);
                        // get author too
                    } else {
                        for (char c : wordDiv.text().toCharArray()) {
                            if (japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(c))) {
                                tempLength++;
                            }
                        }

                        if(tempLength == wordLength && wordLength != 0 && !wordDiv.text().equals(author)) {
                            words.add(wordDiv.text());
                        }
                    }
                }

                System.out.println("this is the words set " + words);
                books.add(new Book("N/A", bookTitle, author, words));
            } catch(IOException e) {
                //
            }

        }
    }

}
