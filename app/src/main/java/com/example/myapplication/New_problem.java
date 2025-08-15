package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class New_problem extends AppCompatActivity {

    private EditText carrierEditText, subTopicEditText, descriptionEditText, remarkEditText;
    private Button btnPickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_problem);

        carrierEditText = findViewById(R.id.carrierEditText);
        subTopicEditText = findViewById(R.id.SubTopicEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        remarkEditText = findViewById(R.id.remarkEditText);
        btnPickDate = findViewById(R.id.btnPickDate);
        FloatingActionButton fab = findViewById(R.id.fab);

        // בחירת תאריך
        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = String.format("%02d/%02d/%04d",
                                dayOfMonth, month1 + 1, year1);
                        btnPickDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        fab.setOnClickListener(v -> {
            String carrier = carrierEditText.getText().toString();
            String subTopic = subTopicEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String remark = remarkEditText.getText().toString();
            String date = btnPickDate.getText().toString();

            ProblemItem item = new ProblemItem(
                    carrier,       // title
                    "",            // topic (ריק אם אין כרגע)
                    subTopic,      // subTopic
                    description,   // description
                    remark,        // remark
                    date           // date
            );

            Intent resultIntent = new Intent();
            resultIntent.putExtra("ProblemItem", item); // שם key חייב להיות זהה ב-Fragment
            setResult(RESULT_OK, resultIntent);
            finish();

            Toast.makeText(New_problem.this, "נוסף פריט חדש!", Toast.LENGTH_SHORT).show();
        });
    }
}
