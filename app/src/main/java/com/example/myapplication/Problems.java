package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Problems extends Fragment {

    private List<String> itemList;
    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_problems, container, false);

        // רשימת נתונים
        itemList = new ArrayList<>();

        // RecyclerView Adapter
        adapter = new MyAdapter(itemList);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // כפתור הוספה + מעבר ל-Activity A
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            // מוסיפים פריט חדש
            itemList.add("New");
            adapter.notifyItemInserted(itemList.size() - 1);
            recyclerView.scrollToPosition(itemList.size() - 1);

            // שולחים לדף בשם A
            Intent intent = new Intent(getActivity(), New_problem.class);
            startActivity(intent);
        });

        return view;
    }
}
