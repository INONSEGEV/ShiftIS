package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class singUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView errorMsg;
    private TextView systemMessage;
    private Button signUpBtn;
    private EditText emailField;
    private EditText passwordField;

    // כאן השתנה: Google Sign-In כפתור רגיל (AppCompatButton)
    private Button googleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sing_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.signUpBtn);
        errorMsg = findViewById(R.id.errorMsg);
        systemMessage = findViewById(R.id.systemMessage);

        signUpBtn.setOnClickListener(v -> performSignUp());

        // הגדרת אפשרויות התחברות בגוגל
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // יש לוודא שהערך קיים ב-strings.xml
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // כפתור Google מותאם אישית (AppCompatButton)
        googleSignInButton = findViewById(R.id.googleSignInBtn);
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                showError("ההתחברות בגוגל נכשלה: " + e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                showSystemMessage("התחברת בהצלחה כ: " + (user != null ? user.getEmail() : "משתמש"));
                Toast.makeText(this, "התחברת עם גוגל!", Toast.LENGTH_SHORT).show();
                // ניתן לנווט הלאה מפה לפי הצורך
            } else {
                showError("שגיאה בהתחברות עם גוגל");
            }
        });
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
            systemMessage.setAlpha(0f);
            systemMessage.animate().alpha(0.8f).setDuration(300).start();
        }
    }

    private void performSignUp() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        hideError();
        showSystemMessage("מבצע הרשמה...");
        signUpBtn.setEnabled(false);
        animateButton(false);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            signUpBtn.setEnabled(true);
            animateButton(true);

            if (task.isSuccessful()) {
                showSystemMessage("נרשמת בהצלחה! ברוך הבא למערכת");
                animateSuccess();
                Toast.makeText(this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();
                signUpBtn.postDelayed(() -> {
                    // הוספת ניווט אם תרצה
                }, 2000);
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
            signUpBtn.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(200).start();
        } else {
            signUpBtn.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.7f).setDuration(200).start();
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

    private String getFirebaseErrorMessage(Exception exception) {
        if (exception == null) return "שגיאה לא ידועה";

        String errorCode = exception.getMessage();
        if (errorCode == null) return "שגיאה לא ידועה";

        if (errorCode.contains("email-already-in-use")) {
            return "כתובת אימייל זו כבר רשומה במערכת";
        } else if (errorCode.contains("invalid-email")) {
            return "כתובת אימייל לא תקינה";
        } else if (errorCode.contains("weak-password")) {
            return "סיסמה חלשה מדי";
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
        if (signUpBtn != null) signUpBtn.clearAnimation();
    }
}
