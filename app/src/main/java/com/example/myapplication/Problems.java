package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.myapplication.Recommendations.RecommendationsItem;
import com.example.myapplication.carrier.AddCreditor;
import com.example.myapplication.carrier.carrierAdapter;
import com.example.myapplication.carrier.carrierItem;
import com.example.myapplication.CarrierRow.CarrierRowItem;
import com.example.myapplication.New_problem;
import com.example.myapplication.standard.standardItem;

import java.util.ArrayList;

public class Problems extends Fragment {

    private carrierAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<carrierItem> items;

    private ActivityResultLauncher<Intent> addCarrierLauncher;
    private ActivityResultLauncher<Intent> editLauncher;
    private ActivityResultLauncher<Intent> newProblemLauncher;
    private ActivityResultLauncher<Intent> editInnerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_problems, container, false);

        Button addButton = view.findViewById(R.id.addButtonCarrier);

        recyclerView = view.findViewById(R.id.recyclerView);
        items = new ArrayList<>();

        adapter = new carrierAdapter(this, items, position -> {
            carrierItem carrierRow = items.get(position);
            Intent intent = new Intent(requireActivity(), New_problem.class);
            intent.putExtra("parentPosition", position);
            intent.putExtra("carrier", carrierRow.getCarrierName());
            newProblemLauncher.launch(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        setupLaunchers();

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
            intent.putStringArrayListExtra("existingNames", getCarrierNames());
            addCarrierLauncher.launch(intent);
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
                            if (getCarrierNames().contains(carrierName)) {
                                Toast.makeText(requireContext(), "Carrier בשם זה כבר קיים!", Toast.LENGTH_SHORT).show();
                                return;
                            }
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
                            for (int i = 0; i < items.size(); i++) {
                                if (i != position && items.get(i).getCarrierName().equals(newCarrierName)) {
                                    Toast.makeText(requireContext(), "Carrier בשם זה כבר קיים!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
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
                        String date = data.getStringExtra("date");
                        ArrayList<Uri> selectedImages = data.getParcelableArrayListExtra("selectedImages");
                        ArrayList<standardItem> standardItems = data.getParcelableArrayListExtra("standardItems");
                        ArrayList<RecommendationsItem> recommendationsItems = data.getParcelableArrayListExtra("recommendationsItems");

                        CarrierRowItem newItem = new CarrierRowItem(carrier, subTopic, description, remark, date, standardItems, recommendationsItems, selectedImages);

                        carrierItem existingCarrier = null;
                        int index = -1;
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getCarrierName().equals(carrier)) {
                                existingCarrier = items.get(i);
                                index = i;
                                break;
                            }
                        }

                        if (existingCarrier != null) {
                            existingCarrier.getInnerItems().add(newItem);
                            adapter.notifyItemChanged(index);
                        } else {
                            carrierItem newCarrier = new carrierItem(carrier);
                            newCarrier.getInnerItems().add(newItem);
                            items.add(newCarrier);
                            adapter.notifyItemInserted(items.size() - 1);
                        }
                    }
                }
        );

        // Edit Inner Item
        editInnerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        int parentIndex = data.getIntExtra("parentIndex", -1);
                        int innerIndex = data.getIntExtra("innerIndex", -1);

                        if (parentIndex != -1 && innerIndex != -1) {
                            String carrier = data.getStringExtra("carrier");
                            String subTopic = data.getStringExtra("subTopic");
                            String description = data.getStringExtra("description");
                            String remark = data.getStringExtra("remark");
                            String date = data.getStringExtra("date");
                            ArrayList<standardItem> standardItems = data.getParcelableArrayListExtra("standardItems");
                            ArrayList<RecommendationsItem> recommendationsItems = data.getParcelableArrayListExtra("recommendationsItems");
                            ArrayList<Uri> selectedImages = data.getParcelableArrayListExtra("selectedImages");

                            CarrierRowItem updatedItem = new CarrierRowItem(carrier, subTopic, description, remark, date,
                                    standardItems, recommendationsItems, selectedImages);

                            carrierItem parent = items.get(parentIndex);
                            parent.getInnerItems().set(innerIndex, updatedItem);
                            adapter.notifyItemChanged(parentIndex);
                        }
                    }
                }
        );
    }

    public void launchEditCarrier(Intent intent) {
        editLauncher.launch(intent);
    }

    public void launchEditInner(Intent intent, int parentIndex, int innerIndex) {
        intent.putExtra("parentIndex", parentIndex);
        intent.putExtra("innerIndex", innerIndex);
        editInnerLauncher.launch(intent);
    }

    public ArrayList<String> getCarrierNames() {
        ArrayList<String> names = new ArrayList<>();
        for (carrierItem item : items) {
            names.add(item.getCarrierName());
        }
        return names;
    }
}
