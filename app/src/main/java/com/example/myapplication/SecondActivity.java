package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btnOpenSecond = findViewById(R.id.btnOpenSecond2);
        TextView txtMessage = findViewById(R.id.txtMessage);
        TextView textView = findViewById(R.id.textViewInSecondPage);

        try {
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            EncryptedSharedPreferences prefs = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    this,
                    "SecureData",  // שם קובץ זהה למה ששמרת בו
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            String savedName = prefs.getString("username", null);

            if (savedName != null) {
                textView.setText("שלום, " + savedName);
            } else {
                txtMessage.setText("לא קיבלתי שם 😅");
            }

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "שגיאה בקריאת הנתונים המוצפנים", Toast.LENGTH_LONG).show();
        }

        btnOpenSecond.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
