package com.example.nene.lifecycle;

import android.nfc.Tag;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PRINT";

    public String name= "name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate");
        TextView tv = findViewById(R.id.editText);
        if (savedInstanceState!=null) {
            Log.i(TAG,"IN");
            tv.setText(savedInstanceState.getCharSequence(name));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        TextView view = findViewById(R.id.editText);
        CharSequence newStr = view.getText();
        Log.i(TAG,newStr.toString());
        savedInstanceState.putCharSequence(name,newStr);
        super.onSaveInstanceState(savedInstanceState);
    }

}
