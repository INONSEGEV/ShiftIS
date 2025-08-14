package com.example.myapplication.ui.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.ui.main.PlaceholderFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        // העברת מספר העמוד ל-Fragment
        return PlaceholderFragment.newInstance(position + 1); // 1, 2, 3
    }

    @Override
    public int getCount() {
        return 3; // מספר העמודים
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "על הבדיקה"; // עמוד 1
            case 1: return "על המזמין";
            case 2: return "תוצאות";
        }
        return null;
    }
}
