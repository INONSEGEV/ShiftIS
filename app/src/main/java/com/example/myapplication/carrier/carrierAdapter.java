package com.example.myapplication.carrier;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CarrierRow.CarrierRowAdapter;
import com.example.myapplication.CarrierRow.CarrierRowItem;
import com.example.myapplication.R;
import com.example.myapplication.Problems;
import com.example.myapplication.carrier.EditCarrier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class carrierAdapter extends RecyclerView.Adapter<carrierAdapter.ViewHolder> {

    private final Problems fragment; // שמירת reference ל-Fragment
    private final ArrayList<carrierItem> items;
    private final Consumer<Integer> onAddNewItemClick;

    public carrierAdapter(Problems fragment, ArrayList<carrierItem> items, Consumer<Integer> onAddNewItemClick) {
        this.fragment = fragment;
        this.items = items;
        this.onAddNewItemClick = onAddNewItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrier_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        carrierItem item = items.get(position);
        holder.carrier.setText(item.getCarrierName());

        // Inner RecyclerView
        CarrierRowAdapter innerAdapter = new CarrierRowAdapter(item.getInnerItems());
        holder.recyclerViewInner.setLayoutManager(new LinearLayoutManager(fragment.requireContext()));
        holder.recyclerViewInner.setAdapter(innerAdapter);

        // כפתור + לשורה זו
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.btnAdd.setOnClickListener(v -> onAddNewItemClick.accept(position));
        }

        // מחיקה
        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                items.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, items.size());
            }
        });

        // עריכה
        holder.btnEdit.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(fragment.requireContext(), EditCarrier.class);
                intent.putExtra("carrier", item.getCarrierName());
                intent.putExtra("position", pos);

                fragment.launchEditCarrier(intent); // שימוש ב-Activity Result API דרך Fragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItemToInner(int parentPosition, CarrierRowItem newItem) {
        if (parentPosition < 0 || parentPosition >= items.size()) return;
        carrierItem parent = items.get(parentPosition);
        parent.getInnerItems().add(newItem);
        notifyItemChanged(parentPosition);
    }

    public void moveItem(int fromPos, int toPos) {
        if (fromPos < 0 || toPos < 0 || fromPos >= items.size() || toPos >= items.size()) return;
        Collections.swap(items, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView carrier;
        RecyclerView recyclerViewInner;
        ImageButton btnAdd;
        ImageButton btnEdit;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carrier = itemView.findViewById(R.id.textView);
            recyclerViewInner = itemView.findViewById(R.id.recyclerViewInner);
            btnAdd = itemView.findViewById(R.id.addButton);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
