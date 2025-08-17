package com.example.myapplication.Recommendations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AddRecommendations extends AppCompatActivity {

    private EditText descriptionEditText, amountEditText, unitPriceEditText, unitEditText, totalPriceEditText;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recommendations);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        unitPriceEditText = findViewById(R.id.unitPriceEditText);
        unitEditText = findViewById(R.id.unitEditText);
        totalPriceEditText = findViewById(R.id.totalPriceEditText);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString();
            String amount = amountEditText.getText().toString();
            String unitPrice = unitPriceEditText.getText().toString();
            String unit = unitEditText.getText().toString();
            String totalPrice = totalPriceEditText.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("description", description);
            resultIntent.putExtra("amount", amount);
            resultIntent.putExtra("unitPrice", unitPrice);
            resultIntent.putExtra("unit", unit);
            resultIntent.putExtra("totalPrice", totalPrice);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}