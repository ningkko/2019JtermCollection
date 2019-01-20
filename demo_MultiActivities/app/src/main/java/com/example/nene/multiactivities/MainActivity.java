package com.example.nene.multiactivities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra(Keys.MESSAGE_KEY,editText.getText().toString());

        startActivityForResult(intent,Keys.REQUEST_NAME);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        String requested = "???";

        if (requestCode == Keys.REQUEST_NAME){
            if (resultCode == RESULT_OK){
                requested = data.getStringExtra(Keys.NAME_KEY);
            }
        }

        editText.setText(requested);

        super.onActivityResult(requestCode, resultCode, data);
    }
}
