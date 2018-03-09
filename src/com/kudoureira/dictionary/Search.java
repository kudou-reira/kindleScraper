package com.kudoureira.dictionary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Search {
    private String vocabulary;
    private ArrayList<Entry> entries = new ArrayList<>();
    private Word searchedWord;

    public Search(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    @Override
    public String toString() {
        return "com.kudoureira.dictionary.Search{" +
                "vocabulary='" + vocabulary + '\'' +
                '}';
    }

    public Word getSearchedWord() {
        return searchedWord;
    }

    public void query() {
        // next is making this concurrent
        // right now, the for loop approaches this sequentially
        // if the book has too many words, we want the fetchData() to run concurrently, not in sequence (for performance reasons)

        try {
            String jishoURL = "http://jisho.org/api/v1/search/words?keyword=";
            String newWord = "";

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(jishoURL);

            try {
                newWord = URLEncoder.encode(vocabulary, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError("UTF-8 is unknown");
            }

            stringBuilder.append(newWord);

            String finalString = stringBuilder.toString();;
            System.out.println("this is the finalString " + finalString);

            URL jishoRequest = new URL(finalString);

            try {
                JSONTokener tokener = new JSONTokener(jishoRequest.openStream());
                JSONObject root = new JSONObject(tokener);
                System.out.println("this is json object " + root);

                System.out.println("starting data load");
                JSONArray data = root.getJSONArray("data");

                for(Object entry : data) {
                    JSONObject castEntry = (JSONObject)(entry);
                    System.out.println("this is entry " + entry);
                    // each entry will be part of an arraylist for the word in question
                    // constructor will have the query

                    // for each entry you get the additional readings and the collection of etymologies
                    // group this under one word
                    JSONArray additionalReadings = castEntry.getJSONArray("japanese");
                    HashMap<String, String> pronounciationCollection = pronounciations(additionalReadings);

                    JSONArray senses = castEntry.getJSONArray("senses");
                    ArrayList<Etymology> etymologyCollection = definition(senses);

                    Entry tempEntry = new Entry(pronounciationCollection, etymologyCollection);

                    System.out.println("this is pronounciationCollection " + pronounciationCollection);
                    System.out.println("this etymology collection " + etymologyCollection);
                    System.out.println("this is new entry");
                    entries.add(tempEntry);

                    // entries is being interfered with by other instantiations of the concurrency
                    System.out.println("this is all entries" + entries);

                    // probably have to make another collection object with the word inside
                }
            } catch(IOException ie) {
                ie.printStackTrace();
            }
            searchedWord = new Word(vocabulary, entries);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    // return an com.kudoureira.dictionary.Etymology object
    private ArrayList<Etymology> definition(JSONArray senses) {
        // overall sense
        ArrayList<Etymology> etymologyCollection = new ArrayList<>();

        for(Object definitions : senses) {
            JSONObject castSenses = (JSONObject)(definitions);
            System.out.println("this is the sense object" + castSenses);

            ArrayList<String> englishDefinition;
            ArrayList<String> partOfSpeech;
            ArrayList<String> tags;
            ArrayList<String> seeAlso;
            ArrayList<String> info;

            englishDefinition = singleSense((JSONArray)(castSenses.get("english_definitions")));
            partOfSpeech = singleSense((JSONArray)(castSenses.get("parts_of_speech")));
            tags = singleSense((JSONArray)(castSenses.get("tags")));
            seeAlso = singleSense((JSONArray)(castSenses.get("see_also")));
            info = singleSense((JSONArray)(castSenses.get("info")));

            Etymology singleEtymology = new Etymology(englishDefinition, partOfSpeech, tags, seeAlso, info);
            etymologyCollection.add(singleEtymology);
            // now put this together with the word in a word object?

            System.out.println("new sense array");
        }
        return etymologyCollection;
    }


    // reusable function for reading in JSONArray data
    private ArrayList<String> singleSense(JSONArray singleSense) {
        ArrayList<String> tempSense = new ArrayList<>();

        System.out.println(singleSense);
        if(singleSense != null) {
            for(int i = 0; i < singleSense.length(); i++) {
                System.out.println(singleSense.get(i));
                tempSense.add((String)singleSense.get(i));
            }
        }
        return tempSense;
    }

    public HashMap<String, String> pronounciations(JSONArray additionalReadings) {
        HashMap<String, String> pronounciations = new HashMap<>();
        String word = "";
        String reading = "";
        String currentWordVal;

        for(Object readings : additionalReadings) {
//            System.out.println("this is readings" + readings);
            JSONObject castReadings = (JSONObject)(readings);
//            System.out.println("this is the jp readings object " + castReadings);

            System.out.println("cast readings" + castReadings);
            if (castReadings.has("reading")) {
                reading = (String)(castReadings.get("reading"));
            }

            if(castReadings.has("reading") && castReadings.has("word")) {
                word = (String)(castReadings.get("word"));

//                System.out.println("this is the reading " + castReadings.get("reading"));
//                System.out.println("this is the word " + castReadings.get("word"));

                if(pronounciations.containsKey(word)) {
                    currentWordVal = pronounciations.get(word);
                    pronounciations.put(word, currentWordVal + ", " + reading);

                } else {
                    pronounciations.put(word, reading);
                }

            } else if(!castReadings.has("reading") && castReadings.has("word")) {
                word = (String)(castReadings.get("word"));
                reading = "no reading provided";
                pronounciations.put(word, reading);
            } else {
                if(pronounciations.containsKey("no kanji")) {
                    currentWordVal = pronounciations.get("no kanji");
                    pronounciations.put("no kanji", currentWordVal + ", " + reading);

                } else {
                    System.out.println("this is used");
                    pronounciations.put("no kanji", reading);
                }
            }
        }

        return pronounciations;
    }
}
