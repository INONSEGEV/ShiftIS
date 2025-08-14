package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.databinding.ActivityNewProjectBinding;

import java.util.Calendar;

public class NewProject extends AppCompatActivity {

    private ActivityNewProjectBinding binding;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate ViewBinding
        binding = ActivityNewProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        MaterialToolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (toolbar.getOverflowIcon() != null) {
            toolbar.getOverflowIcon().setTint(getResources().getColor(android.R.color.white));
        }

        // ViewPager + TabLayout
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        viewPager.setRotationY(180f);

        // FAB – פעולה מותאמת אישית
        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, "פעולה מותאמת אישית", Snackbar.LENGTH_LONG)
                        .setAnchorView(binding.fab)
                        .setAction("Action", null)
                        .show()
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // פונקציות עזר לגישה לתאריך ותמונה
    public void showDatePicker(Button btnPickDate) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    btnPickDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    public void showImagePickerOptions(ImageView imageView) {
        String[] options = {"בחר מהגלריה", "צלם תמונה"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("בחר תמונה")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // גלריה
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "בחר תמונה"), PICK_IMAGE_REQUEST);
                    } else {
                        // מצלמה
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // כאן אפשר לעדכן את ה־ImageView של Fragment הפעיל דרך interface או דרך Activity
    }
}
