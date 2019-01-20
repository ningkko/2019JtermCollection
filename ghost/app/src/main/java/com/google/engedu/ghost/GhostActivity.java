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

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {

    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private Handler handler=new Handler();

    private Button challenge,startNew;
    public TextView text,label,LABEL_point,LABEL_life;

    public String wordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        // resume
        if (savedInstanceState!=null) {
            text.setText(savedInstanceState.getCharSequence(GameData.storedText));
            label.setText(savedInstanceState.getCharSequence(GameData.storedLabel));
        }

        AssetManager assetManager = getAssets();
        label = (TextView) findViewById(R.id.gameStatus);
        text = (TextView) findViewById(R.id.ghostText);
        LABEL_life = (TextView) findViewById(R.id.life);
        LABEL_point = (TextView) findViewById(R.id.points);
        challenge = (Button) findViewById(R.id.challenge);
        startNew = (Button) findViewById(R.id.startNew);

        wordFragment=new String();

        try {
            dictionary=new SimpleDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        challenge.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                onChallenge();

            }
        });

        startNew.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(null);
            }
        });

        onStart(null);


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putCharSequence(GameData.storedText,text.getText());
        savedInstanceState.putCharSequence(GameData.storedLabel,label.getText());

        super.onSaveInstanceState(savedInstanceState);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {

        GameData.inGame=true;
        challenge.setEnabled(true);

        wordFragment="";
        userTurn = random.nextBoolean();

        if (userTurn){
            GameData.PCfirst = false;
        }else{
            userTurn = true;
        }

        text.setText("");

        checkTurn();
        return true;
    }

    public void switchTurn(){
        if (userTurn){
            userTurn=false;
        }else{
            userTurn=true;
        }

    }
    public void checkTurn(){

        if (userTurn) {

            label.setText(GameData.USER_TURN);

        } else {
            label.setText(GameData.COMPUTER_TURN);

            computerTurn();
        }
    }

    public boolean onChallenge() {

        if (wordFragment.length()>3){
            if (userTurn){
                label.setText("User challenges");
            }else{
                label.setText("Computer challenges");
            }

            // human wins if a valid word is built
            if (checkCompleteWord()){
                indicateHumanVictory();
            }else{

                String longerWord=dictionary.getAnyWordStartingWith(wordFragment);

                // if ai can get a valid word, ai wins
                if (longerWord!=null){
                    indicateAIVictory();
                    text.setText(longerWord);
                }else{
                    // if no valid word can be formed any more, human wins
                    indicateHumanVictory();
                }
            }
        }else{
            label.setText("Word too short to be challenged.. Detecting if wrong prefix");
        }
        return true;
    }

    private void computerTurn() {

        // Do computer turn stuff then make it the user's turn again

        if (checkCompleteWord()){
            indicateAIVictory();
            return;
        }

        String longerWord=dictionary.getGoodWordStartingWith(wordFragment);

        if (longerWord==null){

            GameData.inGame=false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    indicateAIVictory();
                }
            }, 1000);

        }else{

            this.wordFragment+=longerWord.substring(wordFragment.length(),wordFragment.length()+1);
            text.setText(wordFragment);
            GameData.totalPoints+=GameData.POINT_PER_LETTER;
            updatePoints();
            userTurn = true;
            label.setText(GameData.USER_TURN);
        }
    }

    public void indicateAIVictory(){
        label.setText("Computer wins!");
        updateLife();

        GameData.inGame = false;
        challenge.setEnabled(false);
    }

    public void indicateHumanVictory(){
        label.setText("Player wins!");
        GameData.totalPoints+=GameData.POINT_PER_TURN;
        updatePoints();

        GameData.inGame = false;
        challenge.setEnabled(false);

    }


    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (GameData.inGame) {
            //a - z-> 29 - 54
            if (keyCode >= 29 && keyCode <= 54) {
                char pressedKey = (char) event.getUnicodeChar();
                this.wordFragment += Character.toString(pressedKey);
                text.setText(wordFragment);
            }
            //
            //        else if (keyCode==67){
            //            if (wordFragment.length()>0) {
            //                wordFragment = wordFragment.substring(0, wordFragment.length() - 1);
            //            }
            //            text.setText(wordFragment);
            //        }

            checkCompleteWord();
            switchTurn();
            checkTurn();
        }

        return super.onKeyUp(keyCode, event);
    }



    public boolean checkCompleteWord(){


        if (dictionary.isWord(wordFragment)&&wordFragment.length()>=GameData.MIN_WORD_LENGTH){
            label.setText("Complete word");
            return true;
        }
        return false;
    }

    public void updateLife(){
        GameData.life--;
        LABEL_life.setText("Life: "+GameData.life);
    }

    public void updatePoints(){
        LABEL_point.setText("Point: "+GameData.totalPoints);
    }

}
