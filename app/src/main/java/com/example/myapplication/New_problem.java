package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class New_problem extends AppCompatActivity {

    private EditText carrierEditText, subTopicEditText, descriptionEditText, remarkEditText;
    private Button btnStandardItem, btnPickDate, btnPickFromGallery, btnOpenCamera;
    private ArrayList<Uri> selectedImages = new ArrayList<>();
    private ImagesAdapter imagesAdapter;

    private RecyclerView recyclerViewStandard, recyclerViewImages;
    private standardAdapter adapter;
    private ArrayList<standardItem> items;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    private static final int ADD_ITEM_REQUEST = 1;
    private static final int EDIT_ITEM_REQUEST = 2;

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
        btnStandardItem = findViewById(R.id.btnStandardItem);
        recyclerViewStandard = findViewById(R.id.recyclerViewStandard);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);

        // ------------------------
        // RecyclerView סטנדרטים
        // ------------------------
        items = new ArrayList<>();
        adapter = new standardAdapter(this, items);
        recyclerViewStandard.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStandard.setAdapter(adapter);
        updateRecyclerViewVisibility();

        btnStandardItem.setOnClickListener(v -> {
            Intent intent = new Intent(New_problem.this, AddStandard.class);
            startActivityForResult(intent, ADD_ITEM_REQUEST);
        });

        // ------------------------
        // RecyclerView תמונות
        // ------------------------
        imagesAdapter = new ImagesAdapter(this, selectedImages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages.setLayoutManager(layoutManager);
        recyclerViewImages.setAdapter(imagesAdapter);
        updateRecyclerViewImagesVisibility();

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
        // Date picker
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
                                imagesAdapter.notifyItemInserted(selectedImages.size() - 1);
                            }
                        } else if (result.getData().getData() != null) {
                            selectedImages.add(result.getData().getData());
                            imagesAdapter.notifyItemInserted(selectedImages.size() - 1);
                        }
                        updateRecyclerViewImagesVisibility();
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
                            int startIndex = selectedImages.size();
                            selectedImages.addAll(cameraImages);
                            imagesAdapter.notifyItemRangeInserted(startIndex, cameraImages.size());
                            updateRecyclerViewImagesVisibility();
                            Toast.makeText(this, cameraImages.size() + " תמונות צולמו", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    // ------------------------
    // טיפול ב-ActivityResult
    // ------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        // הוספת פריט חדש
        if (requestCode == ADD_ITEM_REQUEST) {
            String standard  = data.getStringExtra("standard");
            standardItem newItem = new standardItem(standard);
            items.add(newItem);
            adapter.notifyItemInserted(items.size() - 1);
            updateRecyclerViewVisibility();
        }

        // עריכת פריט קיים
        if (requestCode == EDIT_ITEM_REQUEST) {
            int position = data.getIntExtra("position", -1);
            String standard = data.getStringExtra("standard");

            if (position != -1) {
                standardItem updatedItem = items.get(position);
                updatedItem.setStandard(standard);
                adapter.notifyItemChanged(position);
                updateRecyclerViewVisibility();
            }
        }
    }

    // ------------------------
    // פונקציות עזר: הסתרה/הצגה של Recyclerview
    // ------------------------
    public void updateRecyclerViewVisibility() {
        if (adapter.getItemCount() == 0) {
            recyclerViewStandard.setVisibility(View.GONE);
        } else {
            recyclerViewStandard.setVisibility(View.VISIBLE);
        }
    }

    public void updateRecyclerViewImagesVisibility() {
        if (imagesAdapter.getItemCount() == 0) {
            recyclerViewImages.setVisibility(View.GONE);
        } else {
            recyclerViewImages.setVisibility(View.VISIBLE);
        }
    }
}
