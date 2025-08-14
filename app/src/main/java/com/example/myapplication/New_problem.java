package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityNewProblemBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class New_problem extends AppCompatActivity {

    private ActivityNewProblemBinding binding;
    private List<Uri> imageUris = new ArrayList<>();
    private ImagesAdapter imagesAdapter;

    private static final int PICK_IMAGES_REQUEST = 100;
    private static final int CAMERA_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewProblemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        imagesAdapter = new ImagesAdapter(imageUris);
        binding.contentMain.recyclerViewImages.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.contentMain.recyclerViewImages.setAdapter(imagesAdapter);

        updateRecyclerVisibility();

        // כפתור בחירת תמונות מהגלריה
        binding.contentMain.btnSelectImage.setOnClickListener(v -> pickImagesFromGallery());

        // כפתור צילום – פותח CameraActivity
        binding.contentMain.btnCaptureImage.setOnClickListener(v -> {
            Intent intent = new Intent(New_problem.this, CameraActivity.class);
            startActivityForResult(intent, CAMERA_REQUEST);
        });

        // בחירת תאריך
        binding.contentMain.btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = String.format("%02d/%02d/%04d",
                                dayOfMonth, month1 + 1, year1);
                        binding.contentMain.btnPickDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void updateRecyclerVisibility() {
        binding.contentMain.recyclerViewImages.setVisibility(imageUris.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void pickImagesFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "בחר תמונות"), PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == PICK_IMAGES_REQUEST) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count && imageUris.size() < 20; i++) {
                    imageUris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                imageUris.add(data.getData());
            }
        } else if (requestCode == CAMERA_REQUEST) {
            ArrayList<Uri> capturedImages = data.getParcelableArrayListExtra("capturedImages");
            if (capturedImages != null) {
                imageUris.addAll(capturedImages);
            }
        }

        imagesAdapter.notifyDataSetChanged();
        updateRecyclerVisibility();
    }
}
