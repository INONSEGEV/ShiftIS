package com.example.myapplication.standard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CameraActivity;
import com.example.myapplication.ImagesAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;

public class AddStandard extends AppCompatActivity {

    private EditText standardEditText;
    private Button btnSave, btnPickFromGallery, btnOpenCamera;
    private ArrayList<Uri> selectedImages = new ArrayList<>();
    private ImagesAdapter imagesAdapter;
    private RecyclerView recyclerViewImages;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_standard);

        standardEditText = findViewById(R.id.standardEditText);
        btnSave = findViewById(R.id.btnSave);
        btnPickFromGallery = findViewById(R.id.btnSelectImage);
        btnOpenCamera = findViewById(R.id.btnCaptureImage);

        // RecyclerView להצגת התמונות
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        imagesAdapter = new ImagesAdapter(this, selectedImages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages.setLayoutManager(layoutManager);
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
                            selectedImages.addAll(cameraImages);
                            imagesAdapter.notifyDataSetChanged();
                            updateRecyclerViewImagesVisibility();
                            Toast.makeText(this, cameraImages.size() + " תמונות צולמו", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // ------------------------
        // כפתורים
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

        btnSave.setOnClickListener(v -> {
            String standard = standardEditText.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("standard", standard);
            resultIntent.putParcelableArrayListExtra("selectedImages", selectedImages);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    public void updateRecyclerViewImagesVisibility() {
        recyclerViewImages.setVisibility(imagesAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }
}
