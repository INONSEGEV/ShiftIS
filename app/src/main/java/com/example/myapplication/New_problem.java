package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class New_problem extends AppCompatActivity {

    private EditText carrierEditText, subTopicEditText, descriptionEditText, remarkEditText;
    private Button btnPickDate, btnPickFromGallery, btnOpenCamera;
    private ArrayList<Uri> selectedImages = new ArrayList<>();
    private ImagesAdapter imagesAdapter;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_problem);

        // EditTexts ו-Buttons
        carrierEditText = findViewById(R.id.carrierEditText);
        subTopicEditText = findViewById(R.id.SubTopicEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        remarkEditText = findViewById(R.id.remarkEditText);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickFromGallery = findViewById(R.id.btnSelectImage);
        btnOpenCamera = findViewById(R.id.btnCaptureImage);
        FloatingActionButton fab = findViewById(R.id.fab);

        // RecyclerView
        RecyclerView recyclerViewImages = findViewById(R.id.recyclerViewImages);
        imagesAdapter = new ImagesAdapter(this, selectedImages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages.setLayoutManager(layoutManager);
        recyclerViewImages.setAdapter(imagesAdapter);

        // ------------------------
        //  Date picker
        // ------------------------
        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new android.app.DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = String.format("%02d/%02d/%04d",
                                dayOfMonth, month1 + 1, year1);
                        btnPickDate.setText(selectedDate);
                    }, year, month, day).show();
        });

        // ------------------------
        //  ActivityResultLaunchers
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
        //  Button listeners
        // ------------------------
        btnPickFromGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            galleryLauncher.launch(intent);
        });

        btnOpenCamera.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            cameraLauncher.launch(intent);
        });

        // ------------------------
        //  FAB - שמירת הפריט
        // ------------------------
        fab.setOnClickListener(v -> {
            String carrier = carrierEditText.getText().toString();
            String subTopic = subTopicEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String remark = remarkEditText.getText().toString();
            String date = btnPickDate.getText().toString();

            ProblemItem item = new ProblemItem(
                    carrier,
                    "",        // topic
                    subTopic,
                    description,
                    remark,
                    date
            );

            Intent resultIntent = new Intent();
            resultIntent.putExtra("ProblemItem", item);
            resultIntent.putParcelableArrayListExtra("selectedImages", selectedImages);
            setResult(RESULT_OK, resultIntent);
            finish();

            Toast.makeText(New_problem.this, "נוסף פריט חדש!", Toast.LENGTH_SHORT).show();
        });
    }
}
