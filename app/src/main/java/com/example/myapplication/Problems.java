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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.myapplication.carrier.AddCreditor;
import com.example.myapplication.carrier.carrierAdapter;
import com.example.myapplication.carrier.carrierItem;
import com.example.myapplication.CarrierRow.CarrierRowItem;
import com.example.myapplication.New_problem;

import java.util.ArrayList;

public class Problems extends Fragment {

    private carrierAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<carrierItem> items;

    // Launchers חדשים
    private ActivityResultLauncher<Intent> addCarrierLauncher;
    private ActivityResultLauncher<Intent> editLauncher;
    private ActivityResultLauncher<Intent> newProblemLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_problems, container, false);

        Button addButton = view.findViewById(R.id.addButtonCarrier);
        Button newProblemButton = view.findViewById(R.id.A);

        recyclerView = view.findViewById(R.id.recyclerView);
        items = new ArrayList<>();

        adapter = new carrierAdapter(this, items, position -> {
            Intent intent = new Intent(requireActivity(), New_problem.class);
            intent.putExtra("parentPosition", position);
            newProblemLauncher.launch(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        setupLaunchers();

        // גרירה
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder target) {
                adapter.moveItem(vh.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AddCreditor.class);
            addCarrierLauncher.launch(intent);
        });

        newProblemButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), New_problem.class);
            newProblemLauncher.launch(intent);
        });

        return view;
    }

    private void setupLaunchers() {
        // Add Carrier
        addCarrierLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        String carrierName = result.getData().getStringExtra("carrier");
                        if (carrierName != null) {
                            carrierItem newItem = new carrierItem(carrierName);
                            items.add(newItem);
                            adapter.notifyItemInserted(items.size() - 1);
                        }
                    }
                }
        );

        // Edit Carrier
        editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        int position = result.getData().getIntExtra("position", -1);
                        String newCarrierName = result.getData().getStringExtra("newCarrier");
                        if (position != -1 && newCarrierName != null) {
                            carrierItem item = items.get(position);
                            item.setCarrierName(newCarrierName);
                            adapter.notifyItemChanged(position);
                        }
                    }
                }
        );

        // New Problem
        newProblemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        int parentPosition = data.getIntExtra("parentPosition", -1);
                        String carrier = data.getStringExtra("carrier");
                        String subTopic = data.getStringExtra("subTopic");
                        String description = data.getStringExtra("description");
                        String remark = data.getStringExtra("remark");

                        CarrierRowItem newItem = new CarrierRowItem(carrier, subTopic, description, remark);

                        if (parentPosition != -1 && parentPosition < items.size()) {
                            adapter.addItemToInner(parentPosition, newItem);
                        } else {
                            carrierItem newCarrier = new carrierItem(carrier);
                            newCarrier.getInnerItems().add(newItem);
                            items.add(newCarrier);
                            adapter.notifyItemInserted(items.size() - 1);
                        }
                    }
                }
        );
    }

    // פונקציה ציבורית שנקראת מה-adapter כדי לערוך Carrier
    public void launchEditCarrier(Intent intent) {
        editLauncher.launch(intent);
    }
}
