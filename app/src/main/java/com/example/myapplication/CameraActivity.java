package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    public static final int CAPTURE_IMAGE_REQUEST = 101;
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final String PREFS_NAME = "camera_prefs";
    private static final String PREF_DENIED_BEFORE = "denied_before";

    private PreviewView previewView;
    private ImageButton btnCapture, btnDone;
    private ImageCapture imageCapture;
    private List<Uri> capturedUris = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        btnCapture = findViewById(R.id.btnCapture);
        btnDone = findViewById(R.id.btnDone);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        checkCameraPermission();

        btnCapture.setOnClickListener(v -> takePhoto());
        btnDone.setOnClickListener(v -> finishWithResult());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            boolean deniedBefore = sharedPreferences.getBoolean(PREF_DENIED_BEFORE, false);
            if (deniedBefore) {
                Toast.makeText(this, "נא לאשר הרשאת מצלמה כדי להשתמש באפליקציה", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                sharedPreferences.edit().putBoolean(PREF_DENIED_BEFORE, true).apply();
                Toast.makeText(this, "הרשאת מצלמה נדחתה", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview
                Preview preview = new Preview.Builder()
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // ImageCapture
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) return;

        File photoFile = new File(getExternalFilesDir(null),
                "IMG_" + System.currentTimeMillis() + ".jpg");

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = Uri.fromFile(photoFile);
                        capturedUris.add(savedUri);
                        Toast.makeText(CameraActivity.this, "תמונה נשמרה", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        exception.printStackTrace();
                        Toast.makeText(CameraActivity.this, "שגיאה בצילום", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void finishWithResult() {
        if (!capturedUris.isEmpty()) {
            getIntent().putParcelableArrayListExtra("capturedImages", new ArrayList<>(capturedUris));
            setResult(RESULT_OK, getIntent());
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
