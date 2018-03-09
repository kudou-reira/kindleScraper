package com.kudoureira.dictionary;

import java.util.ArrayList;

public class Etymology {
    private ArrayList<String> englishDefinition;
    private ArrayList<String> partOfSpeech;
    private ArrayList<String> tags;
    private ArrayList<String> seeAlso;
    private ArrayList<String> info;

    public Etymology(ArrayList<String> englishDefinition, ArrayList<String> partOfSpeech, ArrayList<String> tags, ArrayList<String> seeAlso, ArrayList<String> info) {
        this.englishDefinition = englishDefinition;
        this.partOfSpeech = partOfSpeech;
        this.tags = tags;
        this.seeAlso = seeAlso;
        this.info = info;
    }

    @Override
    public String toString() {
        return "com.kudoureira.dictionary.Etymology{" +
                "englishDefinition=" + englishDefinition +
                ", partOfSpeech=" + partOfSpeech +
                ", tags=" + tags +
                ", seeAlso=" + seeAlso +
                ", info=" + info +
                '}';
    }
}
