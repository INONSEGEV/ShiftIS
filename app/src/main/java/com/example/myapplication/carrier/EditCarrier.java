package com.example.myapplication.carrier;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class EditCarrier extends AppCompatActivity {

    private EditText carrierEditText;
    private Button btnSaveEdit;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_carrier);

        carrierEditText = findViewById(R.id.carrierEditText);
        btnSaveEdit = findViewById(R.id.btnSaveEdit);

        // קבלת הנתונים מה־Intent
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        String carrier = intent.getStringExtra("carrier");

        carrierEditText.setText(carrier);

        // שמירת העריכה וחזרה ל־Fragment/Activity
        btnSaveEdit.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position", position);
            resultIntent.putExtra("newCarrier", carrierEditText.getText().toString()); // שם ברור לערך החדש
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
