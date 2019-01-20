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

package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {

    private ArrayList<String> words;
    Random rand=new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {

        if (prefix==null||prefix==""){
            return words.get(rand.nextInt(words.size()));

        }else{

            int index;

            index = BinarySearch.middleBS(words,prefix);
            if (index!=-1){
                return words.get(index);
            }else{
                return null;
            }
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {

        int leftIndex, rightIndex,randomIndex;

        if (prefix==null||prefix==""){
            return words.get(rand.nextInt(words.size()));

        }else{

            leftIndex = BinarySearch.leftBS(words,prefix);
            rightIndex = BinarySearch.rightBS(words,prefix);

            Log.i(GameData.TAG,"left: "+leftIndex+"    ; right: "+rightIndex);

            //if not found
            if (leftIndex==-1||rightIndex==-1){
                return null;
            }
            // if left==right
            else if(leftIndex==rightIndex){
                return words.get(leftIndex);
            }
            else if(leftIndex>rightIndex){
                return null;
            }

            //else return random

            randomIndex = rand.nextInt(rightIndex-leftIndex)+leftIndex;

            String selected;
            int lookTime = 0;

            while (true){

                selected = words.get(randomIndex);

                if ((selected.length()%2==0)&&!GameData.PCfirst){
                    break;
                }

                else if ((selected.length()%2==1)&&GameData.PCfirst){
                    break;
                }

                else if (lookTime>=200){
                    break;
                }

                lookTime++;
            }
            return selected;

        }


    }




}
