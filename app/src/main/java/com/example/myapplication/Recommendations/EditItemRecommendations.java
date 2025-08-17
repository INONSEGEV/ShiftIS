package com.example.myapplication.Recommendations;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class EditItemRecommendations extends AppCompatActivity {

    private EditText descriptionEditText, amountEditText, unitPriceEditText, unitEditText, totalPriceEditText;
    private Button btnSaveEdit;
    private int position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_recommendations);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        unitPriceEditText = findViewById(R.id.unitPriceEditText);
        unitEditText = findViewById(R.id.unitEditText);
        totalPriceEditText = findViewById(R.id.totalPriceEditText);

        btnSaveEdit = findViewById(R.id.btnSaveEdit);

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
        unitEditText.setText(unit);
        totalPriceEditText.setText(totalPrice);


        btnSaveEdit.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position1", position1);
            resultIntent.putExtra("description", descriptionEditText.getText().toString());
            resultIntent.putExtra("amount", amountEditText.getText().toString());
            resultIntent.putExtra("unitPrice", unitPriceEditText.getText().toString());
            resultIntent.putExtra("unit", unitEditText.getText().toString());
            resultIntent.putExtra("totalPrice", totalPriceEditText.getText().toString());

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}

