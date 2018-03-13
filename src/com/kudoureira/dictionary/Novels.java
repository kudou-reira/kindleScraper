package com.kudoureira.dictionary;

import com.kudoureira.crawlKindle.Book;
import com.kudoureira.crawlKindle.ParsedBook;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Novels {
    private ArrayList<Book> novels = new ArrayList<>();
    private ArrayList<ParsedBook> parsedCollection = new ArrayList<>();

    public Novels(ArrayList<Book> novels) {
        this.novels = novels;
    }

    public void compile() {
        // each book will have words sent in as an array list
        // however, probably want these to be separated
        // jisho should take a single word
        // another class should hold the collection of jisho
//        List<String> book1 = Arrays.asList("一", "頂", "理");
//        List<String> book1 = Arrays.asList("一", "頂");
//        List<String> book2 = Arrays.asList("詰め", "堂", "射");
//        List<String> book3 = Arrays.asList("吐き出", "綱渡り", "赤外");
//        List<String> book4 = Arrays.asList("押しつける", "反省", "自覚");

//        ArrayList<List> tempList = new ArrayList<>();

//        tempList.add(book1);
//        tempList.add(book2);
//        tempList.add(book3);
//        tempList.add(book4);


        ExecutorService executor = Executors.newWorkStealingPool();

        // later on have to create the array of words in books?
        // here, it initializes jisho book (collection of words), but maybe it's better to initialize word by word in a different class
        // iterate over arraylist of Lists and add lambda functions to them
        List<Callable<Jisho>> callables = createLists();

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
                    .forEach((book) -> {
                        System.out.println("this is book " + book);
                        book.fetchData();
                        System.out.println("this is FINAL book searched words " + book.getSearchedWords().toString());
                        // probably next should take this function and convert to csv
                        ArrayList<Word> bookWords = book.getSearchedWords();
                        ParsedBook parse = new ParsedBook(book.getBookTitle(), book.getCreators(), bookWords);
                        parsedCollection.add(parse);

//                        JSON tempJSON = new JSON(bookWords);
//                        System.out.println("should pretty print");
//
//                        tempJSON.createJSON();
//                        try {
//                            tempJSON.postJSON();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    });
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        JSON tempJSON = new JSON(parsedCollection);
        System.out.println("should pretty print");

//        tempJSON.createJSON();
//        try {
//            tempJSON.fileJSON();
//        } catch(IOException e) {
//            //
//        }

        try {
            tempJSON.postJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Callable<Jisho>> createLists() {
        List<Callable<Jisho>> tempCallables = new ArrayList<>();

        for(Book temp : novels) {
            // i think instantiate the book title too
            List<String> words = new ArrayList<>(temp.getWords());
            tempCallables.add(() -> new Jisho(words, temp.getBookTitle(), temp.getCreators()));
        }
        return tempCallables;
    }
}