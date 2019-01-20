package com.example.nene.multiactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.security.Key;

public class Activity2 extends AppCompatActivity {

    EditText editText2;
    TextView requested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        editText2 = findViewById(R.id.editText2);
        requested = findViewById(R.id.requested);

        Intent intent = getIntent();

        String message = intent.getStringExtra(Keys.MESSAGE_KEY);

        editText2.setText(message);
        intent.putExtra(Keys.NAME_KEY,requested.getText().toString());
        setResult(RESULT_OK, intent);
    }
}
