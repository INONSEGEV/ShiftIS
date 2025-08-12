package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        Button myButton = findViewById(R.id.myButton);
        EditText nameInput = findViewById(R.id.nameInput);
        TextView txtGreeting = findViewById(R.id.txtGreeting);

        myButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            if (!name.isEmpty()) {
                txtGreeting.setText("שלום " + name + "!");
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("user_name", name); // שולח את המידע
                startActivity(intent);
            } else {
                txtGreeting.setText("הכנס שם קודם");
            }
        });

        Button btnOpenSecond = findViewById(R.id.btnOpenSecond);
        btnOpenSecond.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, singUp.class);
            startActivity(intent);
        });

        EditText editTextName = findViewById(R.id.nameInput);
        Button buttonSave = findViewById(R.id.myButton);
        TextView textViewResult = findViewById(R.id.txtGreeting);

        try {
            // יצירת מפתח אבטחה
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // יצירת SharedPreferences מוצפן
            SharedPreferences prefs = EncryptedSharedPreferences.create(
                    this,
                    "SecureData",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // טוען נתונים אם קיימים
            String savedName = prefs.getString("username", null);
            if (savedName != null) {
                textViewResult.setText("שם שמור: " + savedName);
            }

            // מאזין ללחיצה על הכפתור
            buttonSave.setOnClickListener(v -> {
                String name = editTextName.getText().toString().trim();
                if (!name.isEmpty()) {
                    prefs.edit().putString("username", name).apply();
                    textViewResult.setText("שם שמור: " + name);
                    Toast.makeText(this, "השם נשמר בצורה מאובטחת!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "שגיאה בהגדרת ההצפנה", Toast.LENGTH_LONG).show();
        }
    }
}
