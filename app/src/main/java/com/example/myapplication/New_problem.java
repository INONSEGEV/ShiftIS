package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Recommendations.AddRecommendations;
import com.example.myapplication.Recommendations.RecommendationsAdapter;
import com.example.myapplication.Recommendations.RecommendationsItem;
import com.example.myapplication.standard.AddStandard;
import com.example.myapplication.standard.standardAdapter;
import com.example.myapplication.standard.standardItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class New_problem extends AppCompatActivity {

    private AutoCompleteTextView carrierEditText, descriptionEditText;
    private EditText subTopicEditText, remarkEditText;
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

    private final List<String> carrierOptions = Arrays.asList("סלון", "פינת אוכל", "מבואה / לובי");
    private final List<String> descriptionOptions = Arrays.asList("סדקים נימיים בקירות", "סדר בקיר");

    private ArrayAdapter<String> carrierAdapter;

    private ImageButton fab;

    public static final int ADD_STANDARD_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;
    public static final int ADD_RECOMMENDATION_REQUEST = 3;
    public static final int EDIT_RECOMMENDATION_REQUEST = 4;

    private int currentParentPosition = -1;
    private int currentInnerPosition = -1; // מיקום פריט פנימי לעריכה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_problem);

        // ---------------------------- Init views ----------------------------
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
        fab = findViewById(R.id.fab);

        Intent intent0 = getIntent();
        if(intent0 != null && intent0.hasExtra("carrier")) {
            carrierEditText.setText(intent0.getStringExtra("carrier"));
        }

        // ---------------------------- FAB שמחזיר את הנתונים ----------------------------
        fab.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("parentIndex", currentParentPosition); // Carrier ראשי
            resultIntent.putExtra("innerIndex", currentInnerPosition);   // פריט פנימי
            resultIntent.putExtra("carrier", carrierEditText.getText().toString());
            resultIntent.putExtra("subTopic", subTopicEditText.getText().toString());
            resultIntent.putExtra("description", descriptionEditText.getText().toString());
            resultIntent.putExtra("remark", remarkEditText.getText().toString());
            resultIntent.putExtra("date", btnPickDate.getText().toString());
            resultIntent.putParcelableArrayListExtra("selectedImages", selectedImages);
            resultIntent.putParcelableArrayListExtra("standardItems", items);
            resultIntent.putParcelableArrayListExtra("recommendationsItems", itemsRecommendations);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // ---------------------------- AutoCompleteTextViews ----------------------------
        carrierAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, carrierOptions);
        carrierEditText.setAdapter(carrierAdapter);
        carrierEditText.setOnClickListener(v -> carrierEditText.showDropDown());

        ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, descriptionOptions);
        descriptionEditText.setAdapter(descriptionAdapter);
        descriptionEditText.setOnClickListener(v -> descriptionEditText.showDropDown());

        // ---------------------------- RecyclerView סטנדרטים ----------------------------
        items = new ArrayList<>();
        adapter = new standardAdapter(this, items);
        recyclerViewStandard.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStandard.setAdapter(adapter);
        updateRecyclerViewVisibility();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(items, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewStandard);

        btnStandardItem.setOnClickListener(v -> {
            Intent intent = new Intent(New_problem.this, AddStandard.class);
            startActivityForResult(intent, ADD_STANDARD_REQUEST);
        });

        // ---------------------------- RecyclerView המלצות ----------------------------
        itemsRecommendations = new ArrayList<>();
        adapterRecommendations = new RecommendationsAdapter(this, itemsRecommendations);
        recyclerViewRecommendations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecommendations.setAdapter(adapterRecommendations);
        updateRecyclerViewRecommendationsVisibility();

        btnRecommendationsItem.setOnClickListener(v -> {
            Intent intent = new Intent(New_problem.this, AddRecommendations.class);
            startActivityForResult(intent, ADD_RECOMMENDATION_REQUEST);
        });

        // ---------------------------- RecyclerView תמונות ----------------------------
        imagesAdapter = new ImagesAdapter(this, selectedImages);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imagesAdapter);
        updateRecyclerViewImagesVisibility();

        // ---------------------------- Pickers ----------------------------
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

        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new android.app.DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> btnPickDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // ---------------------------- ActivityResultLaunchers ----------------------------
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                ArrayList<Uri> cameraImages = result.getData().getParcelableArrayListExtra("capturedImages");
                if (cameraImages != null) {
                    int startIndex = selectedImages.size();
                    selectedImages.addAll(cameraImages);
                    imagesAdapter.notifyItemRangeInserted(startIndex, cameraImages.size());
                    updateRecyclerViewImagesVisibility();
                }
            }
        });

        // ---------------------------- מצב עריכה ----------------------------
        if (getIntent() != null) {
            currentParentPosition = getIntent().getIntExtra("parentIndex", -1);
            currentInnerPosition = getIntent().getIntExtra("innerIndex", -1);

            carrierEditText.setText(getIntent().getStringExtra("carrier"));
            subTopicEditText.setText(getIntent().getStringExtra("subTopic"));
            descriptionEditText.setText(getIntent().getStringExtra("description"));
            remarkEditText.setText(getIntent().getStringExtra("remark"));
            btnPickDate.setText(getIntent().getStringExtra("date"));

            ArrayList<Uri> incomingImages = getIntent().getParcelableArrayListExtra("selectedImages");
            if (incomingImages != null) {
                selectedImages.addAll(incomingImages);
                imagesAdapter.notifyDataSetChanged();
                updateRecyclerViewImagesVisibility();
            }

            ArrayList<standardItem> incomingStandards = getIntent().getParcelableArrayListExtra("standardItems");
            if (incomingStandards != null) {
                items.addAll(incomingStandards);
                adapter.notifyDataSetChanged();
                updateRecyclerViewVisibility();
            }

            ArrayList<RecommendationsItem> incomingRecommendations = getIntent().getParcelableArrayListExtra("recommendationsItems");
            if (incomingRecommendations != null) {
                itemsRecommendations.addAll(incomingRecommendations);
                adapterRecommendations.notifyDataSetChanged();
                updateRecyclerViewRecommendationsVisibility();
            }
        }
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

        if (requestCode == EDIT_ITEM_REQUEST) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                String updatedStandard = data.getStringExtra("standard");
                ArrayList<Uri> updatedImages = data.getParcelableArrayListExtra("selectedImages");

                standardItem updatedItem = items.get(position);
                updatedItem.setStandard(updatedStandard);
                if (updatedImages != null) updatedItem.setImages(updatedImages);

                adapter.notifyItemChanged(position);
                updateRecyclerViewVisibility();
            }
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

        if (requestCode == EDIT_RECOMMENDATION_REQUEST) {
            int position = data.getIntExtra("position1", -1);
            if (position != -1) {
                String description = data.getStringExtra("description");
                String amount = data.getStringExtra("amount");
                String unitPrice = data.getStringExtra("unitPrice");
                String unit = data.getStringExtra("unit");
                String totalPrice = data.getStringExtra("totalPrice");

                RecommendationsItem updatedItem = itemsRecommendations.get(position);
                updatedItem.setDescription(description);
                updatedItem.setAmount(amount);
                updatedItem.setUnitPrice(unitPrice);
                updatedItem.setUnit(unit);
                updatedItem.setTotalPrice(totalPrice);

                adapterRecommendations.notifyItemChanged(position);
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
