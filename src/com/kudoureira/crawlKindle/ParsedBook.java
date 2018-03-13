package com.kudoureira.crawlKindle;

import com.kudoureira.dictionary.Word;

import java.util.ArrayList;

public class ParsedBook{
    private String title;
    private String creators;
    private ArrayList<Word> bookWords;

    public ParsedBook(String title, String creators, ArrayList<Word> bookWords) {
        this.title = title;
        this.creators = creators;
        this.bookWords = bookWords;
    }
}
