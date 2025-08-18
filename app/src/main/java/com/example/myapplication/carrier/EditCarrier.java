package com.example.myapplication.carrier;




import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class EditCarrier extends AppCompatActivity {

    private EditText carrierEditText;
    private Button btnSaveEdit;
    private int position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_carrier);

        carrierEditText = findViewById(R.id.carrierEditText);

        btnSaveEdit = findViewById(R.id.btnSaveEdit);

        Intent intent = getIntent();
        position1 = intent.getIntExtra("position", -1);
        String carrier = intent.getStringExtra("carrier");

        carrierEditText.setText(carrier);


        btnSaveEdit.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position", position1);
            resultIntent.putExtra("carrier", carrierEditText.getText().toString());

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
