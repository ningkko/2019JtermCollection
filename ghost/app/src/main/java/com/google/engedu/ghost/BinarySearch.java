package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;

public class BinarySearch {

    private static final String TAG = "PRINT";
    /**
     * Looks for the middle word which contains the target prefix
     * @param words
     * @param prefix
     * @return indexOfTarget
     */
    public static int middleBS(ArrayList<String> words, String prefix){

        int l = 0, r = words.size()-1, mid;
        while (l <= r){
            mid = (int)Math.floor((l+r) / 2);

            if (words.get(mid).startsWith(prefix)){
                return mid;
            }

            if (words.get(mid).compareTo(prefix) > 0){
                r = mid-1;
            } else {
                l = mid+1;
            }
        }
        return -1;

    }


    /**
     * Looks for the leftmost word which contains the target prefix
     *
     * @param words
     * @param prefix
     * @return target index
     */
    public static int leftBS(ArrayList<String> words, String prefix){

        int l = 0, r = words.size(), mid;

        while (l < r){

            mid = (int)Math.floor((l+r) / 2);
            if (words.get(mid).compareTo(prefix) < 0){
                l= mid +1;
            } else {
                r = mid;
            }
        }

        return l;
    }


    /**
     * Looks for the rightmost word which contains the target prefix
     *
     * @param words
     * @param prefix
     * @return target word
     */
    public static int rightBS(ArrayList<String> words, String prefix){

        int l = 0, r = words.size(), mid;

        while (l < r){

            mid = (int)Math.floor((l+r) / 2);

            if (words.get(mid).compareTo(prefix) < 0||words.get(mid).startsWith(prefix)){
                l= mid +1;
            } else {
                r = mid;
            }
        }


        return l-1;
    }

}
