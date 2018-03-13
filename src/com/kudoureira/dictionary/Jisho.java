package com.kudoureira.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Jisho {
    private List<String> wordList;
    private String bookTitle;
    private String creators;
    private ArrayList<Word> searchedWords = new ArrayList<>();

    public Jisho(List<String> wordList, String bookTitle, String creators) {
        this.wordList = wordList;
        this.bookTitle = bookTitle;
        this.creators = creators;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getCreators() {
        return creators;
    }

    @Override
    public String toString() {
        return "com.kudoureira.dictionary.Jisho{" +
                "wordList=" + wordList +
                ", searchedWords=" + searchedWords +
                '}';
    }

    public ArrayList<Word> getSearchedWords() {
        return searchedWords;
    }

    private List<Callable<Search>> createVocabulary() {
        List<Callable<Search>> tempCallables = new ArrayList<>();
        for(String temp : wordList) {
            tempCallables.add(() -> new Search(temp));
        }
        return tempCallables;
    }

    public void fetchData() {

        ExecutorService executor = Executors.newWorkStealingPool();

        // later on have to create the array of words in books?
        // here, it initializes jisho book (collection of words), but maybe it's better to initialize word by word in a different class
        // iterate over arraylist of Lists and add lambda functions to them
        List<Callable<Search>> callables = createVocabulary();

        // the executor invokes all instantiating of a new object () -> new com.kudoureira.dictionary.Jisho(book1); etc
        try {
            executor.invokeAll(callables)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        }
                        catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .forEach((vocabulary) -> {
                        vocabulary.query();
                        searchedWords.add(vocabulary.getSearchedWord());
                    });
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }


}
