package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Problems extends Fragment {

    private List<ProblemItem> itemList;
    private MyAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_problems, container, false);

        itemList = new ArrayList<>();
        adapter = new MyAdapter(itemList);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Launcher לקבלת ProblemItem מ-New_problem
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        ProblemItem item = (ProblemItem) result.getData().getSerializableExtra("ProblemItem");
                        if (item != null) {
                            itemList.add(item);
                            adapter.notifyItemInserted(itemList.size() - 1);
                            recyclerView.scrollToPosition(itemList.size() - 1);
                        }
                    }
                }
        );

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), New_problem.class);
            launcher.launch(intent);
        });

        return view;
    }
}
