package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EditStandard extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private EditText standardEditText;
    private Button btnSave, btnPickFromGallery, btnOpenCamera;
    private int position;
    private ArrayList<Uri> selectedImages = new ArrayList<>();
    private ImagesAdapter imagesAdapter;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_standard);

        checkPermissions(); // <- בקשת הרשאות

        standardEditText = findViewById(R.id.standardEditText);
        btnSave = findViewById(R.id.btnSave);
        btnPickFromGallery = findViewById(R.id.btnSelectImage);
        btnOpenCamera = findViewById(R.id.btnCaptureImage);

        // קבלת הנתונים מה-Intent
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        String standard = intent.getStringExtra("standard");
        ArrayList<Uri> existingImages = intent.getParcelableArrayListExtra("selectedImages");
        if (existingImages != null) selectedImages.addAll(existingImages);

        standardEditText.setText(standard);

        // RecyclerView להצגת התמונות
        RecyclerView recyclerViewImages = findViewById(R.id.recyclerViewImages);
        imagesAdapter = new ImagesAdapter(this, selectedImages);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imagesAdapter);

        // ------------------------
        // ActivityResultLaunchers
        // ------------------------
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        if (result.getData().getClipData() != null) {
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                selectedImages.add(imageUri);
                            }
                        } else if (result.getData().getData() != null) {
                            selectedImages.add(result.getData().getData());
                        }
                        imagesAdapter.notifyDataSetChanged();
                        Toast.makeText(this, selectedImages.size() + " תמונות נבחרו", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<Uri> cameraImages = result.getData().getParcelableArrayListExtra("capturedImages");
                        if (cameraImages != null) {
                            selectedImages.addAll(cameraImages);
                            imagesAdapter.notifyDataSetChanged();
                            Toast.makeText(this, cameraImages.size() + " תמונות צולמו", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // ------------------------
        // כפתורים
        // ------------------------
        btnPickFromGallery.setOnClickListener(v -> openGallery());
        btnOpenCamera.setOnClickListener(v -> openCamera());

        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position", position);
            resultIntent.putExtra("standard", standardEditText.getText().toString());
            resultIntent.putParcelableArrayListExtra("selectedImages", selectedImages);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryLauncher.launch(intent);
    }

    private void openCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        cameraLauncher.launch(intent);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "נדרשות הרשאות כדי לגשת לתמונות/מצלמה", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
}
