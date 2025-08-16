package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddStandard extends AppCompatActivity {

    private EditText standardEditText, imageUri;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_standard);

        standardEditText = findViewById(R.id.standardEditText);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String thing1 = standardEditText.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("standard", thing1);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}

