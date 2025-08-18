package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Recommendations.AddRecommendations;
import com.example.myapplication.Recommendations.RecommendationsAdapter;
import com.example.myapplication.standard.AddStandard;
import com.example.myapplication.standard.standardAdapter;
import com.example.myapplication.Recommendations.RecommendationsItem;
import com.example.myapplication.standard.standardItem;

import java.util.ArrayList;
import java.util.Calendar;

public class New_problem extends AppCompatActivity {

    private EditText carrierEditText, subTopicEditText, descriptionEditText, remarkEditText;
    private Button btnStandardItem, btnPickDate, btnPickFromGallery, btnOpenCamera, btnRecommendationsItem;
    private ArrayList<Uri> selectedImages = new ArrayList<>();
    private ImagesAdapter imagesAdapter;

    private RecyclerView recyclerViewStandard, recyclerViewImages, recyclerViewRecommendations;
    private standardAdapter adapter;
    private RecommendationsAdapter adapterRecommendations;
    private ArrayList<standardItem> items;
    private ArrayList<RecommendationsItem> itemsRecommendations;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    public static final int ADD_STANDARD_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;
    public static final int ADD_RECOMMENDATION_REQUEST = 3;
    public static final int EDIT_ITEM_RECOMMENDATION_REQUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_problem);

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
        recyclerViewRecommendations = findViewById(R.id.recyclerViewRecommendations);
        btnRecommendationsItem = findViewById(R.id.btnRecommendationsItem);

        // ------------------------
        // RecyclerView סטנדרטים
        // ------------------------
        items = new ArrayList<>();
        adapter = new standardAdapter(this, items);
        recyclerViewStandard.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStandard.setAdapter(adapter);
        updateRecyclerViewVisibility();

        // ------------------------
        // RecyclerView המלצות
        // ------------------------
        itemsRecommendations = new ArrayList<>();
        adapterRecommendations = new RecommendationsAdapter(this, itemsRecommendations);
        recyclerViewRecommendations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecommendations.setAdapter(adapterRecommendations);
        updateRecyclerViewRecommendationsVisibility();

        btnStandardItem.setOnClickListener(v -> {
            Intent intent = new Intent(New_problem.this, AddStandard.class);
            startActivityForResult(intent, ADD_STANDARD_REQUEST);
        });

        btnRecommendationsItem.setOnClickListener(v -> {
            Intent intent1 = new Intent(New_problem.this, AddRecommendations.class);
            startActivityForResult(intent1, ADD_RECOMMENDATION_REQUEST);
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
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == ADD_STANDARD_REQUEST) {
            String standard = data.getStringExtra("standard");
            ArrayList<Uri> newImages = data.getParcelableArrayListExtra("selectedImages");
            standardItem newItem = new standardItem(standard);
            if (newImages != null) newItem.setImages(newImages);
            items.add(newItem);
            adapter.notifyItemInserted(items.size() - 1);
            updateRecyclerViewVisibility();
        }

        if (requestCode == ADD_RECOMMENDATION_REQUEST) {
            String description = data.getStringExtra("description");
            String amount = data.getStringExtra("amount");
            String unitPrice = data.getStringExtra("unitPrice");
            String unit = data.getStringExtra("unit");
            String totalPrice = data.getStringExtra("totalPrice");

            RecommendationsItem newItem = new RecommendationsItem(description, amount, unitPrice, unit, totalPrice);
            itemsRecommendations.add(newItem);
            adapterRecommendations.notifyItemInserted(itemsRecommendations.size() - 1);
            updateRecyclerViewRecommendationsVisibility();
        }

        if (requestCode == EDIT_ITEM_REQUEST) {
            int position = data.getIntExtra("position", -1);
            String standard = data.getStringExtra("standard");
            ArrayList<Uri> updatedImages = data.getParcelableArrayListExtra("selectedImages");

            if (position != -1) {
                standardItem updatedItem = items.get(position);
                updatedItem.setStandard(standard);
                if (updatedImages != null) {
                    updatedItem.setImages(updatedImages);
                }
                adapter.notifyItemChanged(position);
                updateRecyclerViewVisibility();
            }
        }

        if (requestCode == EDIT_ITEM_RECOMMENDATION_REQUEST) {
            int position1 = data.getIntExtra("position1", -1);
            String description = data.getStringExtra("description");
            String amount = data.getStringExtra("amount");
            String unitPrice = data.getStringExtra("unitPrice");
            String unit = data.getStringExtra("unit");
            String totalPrice = data.getStringExtra("totalPrice");

            if (position1 != -1) {
                RecommendationsItem updatedItem = itemsRecommendations.get(position1);
                updatedItem.setDescription(description);
                updatedItem.setAmount(amount);
                updatedItem.setUnitPrice(unitPrice);
                updatedItem.setUnit(unit);
                updatedItem.setTotalPrice(totalPrice);
                adapterRecommendations.notifyItemChanged(position1);
                updateRecyclerViewRecommendationsVisibility();
            }
        }
    }

    public void updateRecyclerViewVisibility() {
        recyclerViewStandard.setVisibility(adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }

    public void updateRecyclerViewRecommendationsVisibility() {
        recyclerViewRecommendations.setVisibility(adapterRecommendations.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }

    public void updateRecyclerViewImagesVisibility() {
        recyclerViewImages.setVisibility(imagesAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }
}
