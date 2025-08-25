package com.example.myapplication.carrier;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class AddCreditor extends AppCompatActivity {

    private Button btnSave;
    private EditText carrierEditText;
    private ArrayList<String> existingNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_creditor);

        btnSave = findViewById(R.id.btnSave);
        carrierEditText = findViewById(R.id.carrierEditText);

        existingNames = getIntent().getStringArrayListExtra("existingNames");
        if (existingNames == null) existingNames = new ArrayList<>();

        btnSave.setOnClickListener(v -> {
            String carrier = carrierEditText.getText().toString().trim();

            if (carrier.isEmpty()) {
                Toast.makeText(this, "אנא הזן שם", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existingNames.contains(carrier)) {
                Toast.makeText(this, "Carrier בשם זה כבר קיים!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("carrier", carrier);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
