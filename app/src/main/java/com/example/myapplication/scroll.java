package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.example.myapplication.databinding.ActivityScrollBinding;
import com.google.android.material.snackbar.Snackbar;

public class scroll extends AppCompatActivity {

    private ActivityScrollBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScrollBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Adapter + ViewPager
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this,getSupportFragmentManager());
        binding.viewPager.setAdapter(sectionsPagerAdapter);

        // מחבר TabLayout ל-ViewPager
        binding.tabs.setupWithViewPager(binding.viewPager);




    }
}
