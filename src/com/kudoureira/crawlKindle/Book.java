package com.kudoureira.crawlKindle;

import java.util.HashSet;

public class Book {
    private String lastAccessed;
    private String bookTitle;
    private String creators;
    private HashSet<String> words;

    public Book(String lastAccessed, String bookTitle, String creators, HashSet<String> words) {
        this.lastAccessed = lastAccessed;
        this.bookTitle = bookTitle;
        this.creators = creators;
        this.words = words;
    }

    public String getLastAccessed() {
        return lastAccessed;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getCreators() {
        return creators;
    }

    public HashSet<String> getWords() {
        return words;
    }
}
