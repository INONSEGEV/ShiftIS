package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditStandard extends AppCompatActivity {

    private EditText standardEditText;
    private Button btnSave;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_standard);

        standardEditText = findViewById(R.id.standardEditText);
        btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        String standard = intent.getStringExtra("standard");

        standardEditText.setText(standard);

        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position", position);
            resultIntent.putExtra("standard", standardEditText.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();

        });
    }
}

