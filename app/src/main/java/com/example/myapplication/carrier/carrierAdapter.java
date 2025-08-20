package com.example.myapplication.carrier;

import android.content.Context;
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

import java.util.Collections;
import java.util.List;

public class carrierAdapter extends RecyclerView.Adapter<carrierAdapter.ItemViewHolder> {

    public interface OnItemEditListener {
        void onEdit(int position, carrierItem item);
    }

    private final Context context;
    private final List<carrierItem> items;
    private final OnItemEditListener listener;

    public carrierAdapter(Context context, List<carrierItem> items, OnItemEditListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.carrier_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        carrierItem carrier = items.get(position);
        holder.carrierName.setText(carrier.getCarrier());

        // RecyclerView פנימי
        if (holder.innerAdapter == null) {
            holder.innerAdapter = new CarrierRowAdapter(context, carrier.getItems());
            holder.recyclerViewInner.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerViewInner.setAdapter(holder.innerAdapter);
            holder.recyclerViewInner.setNestedScrollingEnabled(false); // חשוב
        }

        // כפתור הוספה
        holder.addItemBtn.setOnClickListener(v -> {
            CarrierRowItem newItem = new CarrierRowItem(carrier.getCarrier(), "Subtopic " + (carrier.getItems().size() + 1));
            carrier.addItem(newItem);
            holder.innerAdapter.addItem(newItem);
        });

        // כפתור מחיקה
        holder.deleteCarrierBtn.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos >= 0 && pos < items.size()) {
                items.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, items.size() - pos);
            }
        });

        // לחיצה לעריכה
        holder.carrierName.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(holder.getAdapterPosition(), carrier);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < items.size() && toPosition < items.size()) {
            Collections.swap(items, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            int start = Math.min(fromPosition, toPosition);
            int end = Math.max(fromPosition, toPosition);
            notifyItemRangeChanged(start, end - start + 1);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView carrierName, positionNumber;
        RecyclerView recyclerViewInner;
        ImageButton addItemBtn, deleteCarrierBtn;
        CarrierRowAdapter innerAdapter;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            carrierName = itemView.findViewById(R.id.textView);
            positionNumber = itemView.findViewById(R.id.positionNumber);
            recyclerViewInner = itemView.findViewById(R.id.recyclerViewRecommendations);
            addItemBtn = itemView.findViewById(R.id.adButton);
            deleteCarrierBtn = itemView.findViewById(R.id.deleteButton);
            innerAdapter = null;
        }
    }
}
