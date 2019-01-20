/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;


import android.support.annotation.IntegerRes;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final String TAG = "PRINT1";
    /**
     * game level, how long should the new world be?
     */
    private int wordLength;
    /**
     * when game starts
     */
    private static final int DEFAULT_WORD_LENGTH = 3;

    /**
     * min num of anagrams allowed
     */
    public static final int MIN_NUM_ANAGRAMS = 5;

    private static final int MAX_WORD_LENGTH = 7;

    /**
     * contains all words
     */
    public HashSet<String> wordSet;
    /**
     * classified map of words based on letters
     */
    public HashMap<String, HashSet<String>> lettersToWord;
    /**
     * classified map of words based on lengths
     */
    public HashMap<Integer,HashSet<String>> sizeToWord;
    private Random random = new Random();


    public AnagramDictionary(Reader reader) throws IOException {

        this.wordSet=new HashSet<>();
        this.lettersToWord = new HashMap<>();
        this.sizeToWord= new HashMap<>();

        this.wordLength = DEFAULT_WORD_LENGTH;

        BufferedReader in = new BufferedReader(reader);
        String line;

        //read in dictionary
        while ((line = in.readLine()) != null) {

            String word = line.trim();
            if (word.length()>2) {
                wordSet.add(word);

                // add to lettersToWord
                char[] ch = word.toCharArray();
                Arrays.sort(ch);
                String key = new String(ch);
                if (!lettersToWord.containsKey(key)) {
                    lettersToWord.put(key, new HashSet<String>());
                }
                this.lettersToWord.get(key).add(word);


                // add to sizeToWord
                if (!sizeToWord.containsKey(word.length())) {
                    sizeToWord.put(word.length(), new HashSet<String>());
                }
                this.sizeToWord.get(word.length()).add(word);
            }
        }

        Log.i(TAG, "Total word count: " + wordSet.size());

    }

    /**
     *Asserts that the given word is in the dictionary and isn't formed by adding a letter
     * to the start or end of the base word.
     * @param word
     * @param base
     * @return boolean
     */
    public boolean isGoodWord(String word, String base) {

        if (!(word.length()==base.length()+1)||!this.wordSet.contains(word)||
                word.substring(1,word.length()-1).equals(base)||
                word.substring(0,word.length()-2).equals(base)){
            return false;
        }

        return true;

    }


    public List<String> getAnagrams(String base) {

        ArrayList<String> result = new ArrayList<String>();

        // get corresponding key of the base
        char[] baseKeyRaw = base.toCharArray();
        Arrays.sort(baseKeyRaw);
        String baseKey = new String(baseKeyRaw);

        // only look at target set
        if (lettersToWord.containsKey(baseKey)){
            for (String word : this.lettersToWord.get(baseKey)) {
                word = word.toLowerCase();
                base = base.toLowerCase();

                //check goodness
                if (!word.equals(base)) {
                    result.add(word);

                }
            }
        }


        Log.i(TAG, result.size() + " matching anagrams found.");
        return result;

    }

    public List<String> getAnagramsWithOneMoreLetter(String base) {

        List<String> result = new ArrayList<>();

        String[] alphabet="abcdefghijklmnopqrstuvwxyz".split("");

        for (String s: alphabet) {

            List<String> rawresult1 = getAnagrams(s + base);
            for (String str1 : rawresult1) {
                if(isGoodWord(str1, base)) {
                    result.add(str1);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord(boolean increaseLevel) {

        if (wordLength<this.MAX_WORD_LENGTH&&increaseLevel) {
            wordLength++;
        }

        List ls = new ArrayList(this.sizeToWord.get(wordLength));
        Collections.shuffle(ls);
        return (String)ls.get(0);
    }
}