package com.example.myapplication;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class PageOneFragment extends Fragment {

    private Button btnPickDate, btnSelectImage;
    private EditText checkerEditText, contractorEditText, presentEditText;
    private ImageView imageView;
    private ImageButton btnDeleteImage;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<Void> captureImageLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_page_one, container, false);

        // איפוס שדות
        btnPickDate = root.findViewById(R.id.btnPickDate);
        btnSelectImage = root.findViewById(R.id.btnSelectImage);
        checkerEditText = root.findViewById(R.id.checkerEditText);
        contractorEditText = root.findViewById(R.id.contractorEditText);
        presentEditText = root.findViewById(R.id.presentEditText);
        imageView = root.findViewById(R.id.imageView);
        btnDeleteImage = root.findViewById(R.id.btnDeleteImage);

        // ActivityResultLauncher לבחירת תמונה מהגלריה
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageView.setImageURI(result);
                            btnDeleteImage.setVisibility(View.VISIBLE);
                        }
                    }
                });

        // ActivityResultLauncher לצילום תמונה
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                new ActivityResultCallback<Bitmap>() {
                    @Override
                    public void onActivityResult(Bitmap result) {
                        if (result != null) {
                            imageView.setImageBitmap(result);
                            btnDeleteImage.setVisibility(View.VISIBLE);
                        }
                    }
                });

        // מאזינים לכפתורים
        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnSelectImage.setOnClickListener(v -> showImageOptions());
        btnDeleteImage.setOnClickListener(v -> {
            imageView.setImageDrawable(null);
            btnDeleteImage.setVisibility(View.GONE);
        });

        return root;
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                (view, y, m, d) -> btnPickDate.setText(d + "/" + (m + 1) + "/" + y),
                year, month, day);

        dialog.show();
    }

    private void showImageOptions() {
        String[] options = {"בחר מהגלריה", "צלם תמונה"};
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("בחר תמונה")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) pickImageLauncher.launch("image/*");
                    else captureImageLauncher.launch(null);
                })
                .show();
    }
}
