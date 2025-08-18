package com.example.myapplication.Recommendations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AddRecommendations extends AppCompatActivity {

    private EditText descriptionEditText, amountEditText, unitPriceEditText, totalPriceEditText;
    private Spinner unitSpinner;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recommendations);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        unitPriceEditText = findViewById(R.id.unitPriceEditText);
        totalPriceEditText = findViewById(R.id.totalPriceEditText);
        unitSpinner = findViewById(R.id.unitSpinner);
        btnSave = findViewById(R.id.btnSave);

        // מערך אופציות ל-Spinner
        String[] units = {"ימים", "ליטר", "מטר", "מ\"ר", "קומפלט", "קוב", "ק\"ג", "שעות", "טון", "יח'", "משטח"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString();
            String amount = amountEditText.getText().toString();
            String unitPrice = unitPriceEditText.getText().toString();
            String unit = unitSpinner.getSelectedItem().toString(); // <-- קבלת הבחירה מה-Spinner
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
