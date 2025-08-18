package com.example.myapplication.carrier;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AddCreditor extends AppCompatActivity {


    private Button btnSave;
    private EditText carrierEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // מחבר את המחלקה ל־a.xml
        setContentView(R.layout.add_creditor);
        btnSave = findViewById(R.id.btnSave);
        carrierEditText = findViewById(R.id.carrierEditText);


        btnSave.setOnClickListener(v -> {
            String carrier = carrierEditText.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("carrier", carrier);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }
}
