package com.example.myapplication.carrier;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class EditCarrier extends AppCompatActivity {

    private EditText carrierEditText;
    private Button btnSaveEdit;
    private int position;
    private ArrayList<String> existingNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_carrier);

        carrierEditText = findViewById(R.id.carrierEditText);
        btnSaveEdit = findViewById(R.id.btnSaveEdit);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        String carrier = intent.getStringExtra("carrier");
        existingNames = intent.getStringArrayListExtra("existingNames");
        if (existingNames == null) existingNames = new ArrayList<>();

        carrierEditText.setText(carrier);

        btnSaveEdit.setOnClickListener(v -> {
            String newCarrier = carrierEditText.getText().toString().trim();

            if (newCarrier.isEmpty()) {
                Toast.makeText(this, "אנא הזן שם", Toast.LENGTH_SHORT).show();
                return;
            }

            // בדיקה אם השם כבר קיים ברשימה (ללא הפריט הנוכחי)
            if (existingNames.contains(newCarrier)) {
                Toast.makeText(this, "Carrier בשם זה כבר קיים!", Toast.LENGTH_SHORT).show();
                return; // כאן מונע סיום ה-Activity
            }

            // רק אם השם חדש – מחזירים את התוצאה ומסיימים
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position", position);
            resultIntent.putExtra("newCarrier", newCarrier);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }
}
