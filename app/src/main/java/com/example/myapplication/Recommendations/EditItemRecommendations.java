package com.example.myapplication.Recommendations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class EditItemRecommendations extends AppCompatActivity {

    private EditText descriptionEditText, amountEditText, unitPriceEditText, totalPriceEditText;
    private Spinner unitSpinner;
    private Button btnSaveEdit;
    private int position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_recommendations);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        unitPriceEditText = findViewById(R.id.unitPriceEditText);
        totalPriceEditText = findViewById(R.id.totalPriceEditText);
        unitSpinner = findViewById(R.id.unitSpinner); // ID מה-XML נשאר כמו שהיה
        btnSaveEdit = findViewById(R.id.btnSaveEdit);

        // מערך אופציות ל-Spinner
        String[] units = {"ימים", "ליטר", "מטר", "מ\"ר", "קומפלט", "קוב", "ק\"ג", "שעות", "טון", "יח'", "משטח"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        // קבלת הנתונים מה-Intent
        Intent intent = getIntent();
        position1 = intent.getIntExtra("position1", -1);
        String description = intent.getStringExtra("description");
        String amount = intent.getStringExtra("amount");
        String unitPrice = intent.getStringExtra("unitPrice");
        String unit = intent.getStringExtra("unit");
        String totalPrice = intent.getStringExtra("totalPrice");

        descriptionEditText.setText(description);
        amountEditText.setText(amount);
        unitPriceEditText.setText(unitPrice);
        totalPriceEditText.setText(totalPrice);

        // קביעת הבחירה הקודמת ב-Spinner
        if (unit != null) {
            int spinnerPosition = adapter.getPosition(unit);
            if (spinnerPosition >= 0) {
                unitSpinner.setSelection(spinnerPosition);
            }
        }

        btnSaveEdit.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position1", position1);
            resultIntent.putExtra("description", descriptionEditText.getText().toString());
            resultIntent.putExtra("amount", amountEditText.getText().toString());
            resultIntent.putExtra("unitPrice", unitPriceEditText.getText().toString());
            resultIntent.putExtra("unit", unitSpinner.getSelectedItem().toString()); // <-- הבחירה מה-Spinner
            resultIntent.putExtra("totalPrice", totalPriceEditText.getText().toString());

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
