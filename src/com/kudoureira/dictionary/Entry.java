package com.kudoureira.dictionary;

import java.util.ArrayList;
import java.util.HashMap;

public class Entry {
    private HashMap<String, String> pronounciations = new HashMap<>();
    private ArrayList<Etymology> etymologies = new ArrayList<>();

    public Entry(HashMap<String, String> pronounciations, ArrayList<Etymology> etymologies) {
        this.pronounciations = pronounciations;
        this.etymologies = etymologies;
    }

    @Override
    public String toString() {
        return "com.kudoureira.dictionary.Entry{" +
                "pronounciations=" + pronounciations +
                ", etymologies=" + etymologies +
                '}';
    }
}
