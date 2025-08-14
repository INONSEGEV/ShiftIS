package com.example.myapplication.ui.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.PageOneFragment;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentPageOneBinding;
import com.example.myapplication.databinding.FragmentScrollBinding;
import com.example.myapplication.invitingFragment;

import java.util.Calendar;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private FragmentScrollBinding scrollBinding;
    private FragmentPageOneBinding pageOneBinding;
    private invitingFragment invitingFragment;
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int index = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER, 1) : 1;

        View root = inflater.inflate(R.layout.fragment_container, container, false);

        if (savedInstanceState == null) {
            if (index == 1) {
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, new PageOneFragment())
                        .commit();
            } else if (index == 2) {
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, new invitingFragment())
                        .commit();
            } else {
                scrollBinding = FragmentScrollBinding.inflate(inflater, container, false);
                scrollBinding.sectionLabel.setText("תוכן של עמוד " + index);
                root = scrollBinding.getRoot();
            }
        }

        root.setRotationY(180f);
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scrollBinding = null;
        pageOneBinding = null;
    }

    // פונקציות עזר
    private void showDatePicker(Button btnPickDate) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getContext(), (view, y, m, d) ->
                btnPickDate.setText(d + "/" + (m + 1) + "/" + y),
                year, month, day).show();
    }

    private void showImageOptions(ImageView imageView) {
        String[] options = {"בחר מהגלריה", "צלם תמונה"};
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("בחר תמונה")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // גלריה
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "בחר תמונה"), 1);
                    } else {
                        // מצלמה
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 2);
                    }
                }).show();
    }
}
