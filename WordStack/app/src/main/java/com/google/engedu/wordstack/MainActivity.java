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

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 3;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    public Stack<Character> shuffled;
    private final String TAG="PRINT";
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private boolean inGame;
    final Handler handler= new Handler();


    TextView messageBox;

    private Stack<LetterTile> placedTiles;
    private LinearLayout verticalLayout;
    private View word1LinearLayout;
    private View word2LinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();

        inGame=false;

        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();

                if(word.length()==WORD_LENGTH) {
                    words.add(word);
                }

            }


        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        messageBox= (TextView) findViewById(R.id.message_box);
        placedTiles=new Stack();
        verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placedTiles.push(tile);
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        @RequiresApi(api = Build.VERSION_CODES.P)
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                        if (checkAns()) {
                        messageBox.setText("Correct! Oh you genius");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onStartGame(null);
                            }
                        }, 4000);
                    }
                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    public String getPlayerAns(int n){

        LinearLayout layout=null;
        int count;

        if (n==1){
            layout=(LinearLayout)word1LinearLayout;
        }else if(n==2){
            layout=(LinearLayout)word2LinearLayout;
        }else{
            Log.i(TAG,"Wrong number passed in at getText()");
        }

        String str = new String();

        TextView v=null;
        if (layout!=null){
            count=layout.getChildCount();
            for (int i=0;i<count;i++){
                v=(TextView)layout.getChildAt(i);
                str+=v.getText();
            }
        }

        Log.i(TAG,"User result: "+str);
        return str;


    }

    public boolean checkAns(){
        if (getPlayerAns(1).equals(word1)&&getPlayerAns(2).equals(word2)){
            return true;
        }else if(getPlayerAns(2).equals(word1)&&getPlayerAns(1).equals(word2)){
            return true;
        }
        return false;
    }
    public boolean onStartGame(View view) {

        messageBox.setText("Pruuuuu");
        stackedLayout.removeAllViews();
        stackedLayout.clear();
        ((LinearLayout)word1LinearLayout).removeAllViews();
        ((LinearLayout)word2LinearLayout).removeAllViews();

        //randomly pick up 2 words
        word1=words.get(random.nextInt(words.size()));
        word2=words.get(random.nextInt(words.size()));
        if (word1.equals(word2)){
            word2=words.get(random.nextInt(words.size()));
        }

        this.shuffled = shuffleWords(word1,word2);
        Log.i(TAG,word1+" + "+word2+" = "+shuffled);

        for (Character c: shuffled) {
            stackedLayout.push(new LetterTile(this,c));
        }

        this.inGame=true;

        return true;
    }

    public Stack<Character> shuffleWords(String a, String b){


        ArrayList<Character>charA=new ArrayList();
        ArrayList<Character> charB=new ArrayList<>();
        Stack<Character> resultReversed=new Stack<>();
        Stack<Character> result=new Stack<>();
        Character c;

        for(char ca:a.toCharArray()){
            charA.add(ca);
        }
        for(char cb:b.toCharArray()){
            charB.add(cb);
        }

        while(charA.size()>0&&charB.size()>0) {

            if (random.nextInt(2) == 0) {
                c = charA.get(0);
                charA.remove(0);
                resultReversed.add(c);

            } else {
                c = charB.get(0);
                charB.remove(0);
                resultReversed.add(c);
            }
        }

        if (charA.size()==0){
            for (int i = 0;i<charB.size();i++){
                resultReversed.add(charB.get(i));
            }
        }
        if (charB.size()==0) {
            for (int i = 0; i < charA.size(); i++) {
                resultReversed.add(charA.get(i));
            }
        }

        while(!resultReversed.empty()){
            Character cha = resultReversed.pop();
            result.add(cha);
        }
        return result;
    }

    public boolean onUndo(View view) {
        if (placedTiles.size()!=0){
            LetterTile popped = placedTiles.pop();
            popped.moveToViewGroup(stackedLayout);
        }
        return true;
    }

    public void onAns(View view){
        if (this.inGame) {
            messageBox.setText(word1 + " & " + word2);
        }
    }

    public List<String> findAllValidAns(){

        String[] s1=word1.split("");
        String[] s2=word2.split("");

        return null;

    }
}
