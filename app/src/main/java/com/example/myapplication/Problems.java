package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

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

import com.example.myapplication.carrier.AddCreditor;
import com.example.myapplication.carrier.EditCarrier;
import com.example.myapplication.carrier.carrierAdapter;
import com.example.myapplication.carrier.carrierItem;

import java.util.ArrayList;

public class Problems extends Fragment {

    private carrierAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<carrierItem> items;

    public static final int ADD_CARRIER_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_problems, container, false);

        Button addButton = view.findViewById(R.id.addButtonCarrier);
        recyclerView = view.findViewById(R.id.recyclerView);

        // ------------------------
        // RecyclerView סטנדרטים
        // ------------------------
        items = new ArrayList<>();

        adapter = new carrierAdapter(requireContext(), items, (position, item) -> {
            // Callback לעריכת פריט
            Intent intent = new Intent(getActivity(), EditCarrier.class);
            intent.putExtra("carrier", item.getCarrier());
            intent.putExtra("position", position);
            startActivityForResult(intent, EDIT_ITEM_REQUEST);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        updateRecyclerViewVisibility();

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AddCreditor.class);
            startActivityForResult(intent, ADD_CARRIER_REQUEST);
        });

        Button A = view.findViewById(R.id.A);
        A.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), New_problem.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == ADD_CARRIER_REQUEST) {
            String carrier = data.getStringExtra("carrier");
            carrierItem newItem = new carrierItem(carrier);
            items.add(newItem);
            adapter.notifyItemInserted(items.size() - 1);
            updateRecyclerViewVisibility();
        }

        if (requestCode == EDIT_ITEM_REQUEST) {
            int position = data.getIntExtra("position", -1);
            String carrier = data.getStringExtra("carrier");

            if (position != -1) {
                carrierItem updatedItem = items.get(position);
                updatedItem.setCarrier(carrier);
                adapter.notifyItemChanged(position);
                updateRecyclerViewVisibility();
            }
        }
    }

    public void updateRecyclerViewVisibility() {
        recyclerView.setVisibility(adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }
}
