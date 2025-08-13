package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class sing extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private CheckBox rememberMeCheckBox;
    private FirebaseAuth auth;
    private SharedPreferences prefs;
    private TextView errorMsg;
    private TextView systemMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        errorMsg = findViewById(R.id.errorMsg);

        auth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // בדיקה אם המשתמש כבר מחובר
        if (auth.getCurrentUser() != null && prefs.getBoolean("rememberMe", false)) {
            startActivity(new Intent(sing.this, MainActivity.class));
            finish();
            return;
        }

        loginButton.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        hideError();
        showSystemMessage("מתחבר למערכת...");
        loginButton.setEnabled(false);
        animateButton(false);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loginButton.setEnabled(true);
                    animateButton(true);

                    if (task.isSuccessful()) {
                        // שמירת מצב "זכור אותי"
                        prefs.edit().putBoolean("rememberMe", rememberMeCheckBox.isChecked()).apply();

                        showSystemMessage("התחברת בהצלחה! ברוך הבא למערכת");
                        animateSuccess();
                        Toast.makeText(this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();

                        loginButton.postDelayed(() -> {
                            startActivity(new Intent(sing.this, Loading_page.class));
                            finish();
                        }, 1000);

                    } else {
                        String errorMessage = getFirebaseErrorMessage(task.getException());
                        showError("שגיאה: " + errorMessage);
                        Toast.makeText(this, "שגיאה: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showError("אנא מלא את כל השדות הנדרשים (אימייל וסיסמה)");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("כתובת אימייל לא תקינה");
            return false;
        }
        if (password.length() < 6) {
            showError("הסיסמה חייבת להיות באורך 6 תווים לפחות");
            return false;
        }
        if (password.length() > 40) {
            showError("הסיסמה חייבת להיות עד 40 תווים");
            return false;
        }
        return true;
    }

    private void animateButton(boolean enabled) {
        if (enabled) {
            loginButton.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(200).start();
        } else {
            loginButton.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.7f).setDuration(200).start();
        }
    }

    private void animateSuccess() {
        View mainContainer = findViewById(R.id.main);
        if (mainContainer != null) {
            mainContainer.animate().scaleX(1.02f).scaleY(1.02f).setDuration(300).withEndAction(() -> {
                mainContainer.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
            }).start();
        }
    }

    private void showError(String message) {
        if (errorMsg != null) {
            errorMsg.setText(message);
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setAlpha(0f);
            errorMsg.setTranslationY(-20f);
            errorMsg.animate().alpha(1f).translationY(0f).setDuration(400)
                    .setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }
    }

    private void hideError() {
        if (errorMsg != null && errorMsg.getVisibility() == View.VISIBLE) {
            errorMsg.animate().alpha(0f).translationY(-20f).setDuration(400)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withEndAction(() -> errorMsg.setVisibility(View.GONE)).start();
        }
    }

    private void showSystemMessage(String message) {
        if (systemMessage != null) {
            systemMessage.setText(message);
            systemMessage.setVisibility(View.VISIBLE);
            systemMessage.setAlpha(0.8f);
            systemMessage.animate().alpha(0.8f).setDuration(300).start();
        }
    }

    private String getFirebaseErrorMessage(Exception exception) {
        if (exception == null) return "שגיאה לא ידועה";

        String errorCode = exception.getMessage();
        if (errorCode == null) return "שגיאה לא ידועה";

        if (errorCode.contains("user-not-found")) {
            return "משתמש לא נמצא במערכת";
        } else if (errorCode.contains("wrong-password")) {
            return "סיסמה שגויה";
        } else if (errorCode.contains("invalid-email")) {
            return "כתובת אימייל לא תקינה";
        } else if (errorCode.contains("user-disabled")) {
            return "חשבון המשתמש נחסם";
        } else if (errorCode.contains("too-many-requests")) {
            return "יותר מדי נסיונות התחברות, נסה שוב מאוחר יותר";
        } else if (errorCode.contains("network-request-failed")) {
            return "בעיית רשת - בדוק את החיבור לאינטרנט";
        } else {
            return errorCode;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (errorMsg != null) errorMsg.clearAnimation();
        if (systemMessage != null) systemMessage.clearAnimation();
        if (loginButton != null) loginButton.clearAnimation();
    }
}