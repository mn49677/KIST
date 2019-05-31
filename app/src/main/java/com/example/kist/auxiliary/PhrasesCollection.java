package com.example.kist.auxiliary;

import com.example.kist.fragments.InputFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class PhrasesCollection {

    ArrayList<String> phrases;
    ArrayList<Integer> usedPhrases;
    InputStream inputStream;

    public PhrasesCollection(InputStream inputStream) {
        usedPhrases = new ArrayList<>();
        phrases = new ArrayList<>();
        this.inputStream = inputStream;
        //load phrases from text file
        try {
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                phrases.add(line);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }
    public String getRandom() {
        Random rand = new Random();
        int r;
        do {
            r = rand.nextInt() % phrases.size();
            if (r < 0) r = -r;
        } while (usedPhrases.contains(r));
        usedPhrases.add(r);
        return phrases.get(r);
    }

}
