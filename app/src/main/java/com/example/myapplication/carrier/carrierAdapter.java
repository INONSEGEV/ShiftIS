package com.example.myapplication.carrier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.Collections;
import java.util.List;

public class carrierAdapter extends RecyclerView.Adapter<carrierAdapter.ItemViewHolder> {

    public interface OnItemClickListener {
        void onEdit(int position, carrierItem item);
    }

    private final Context context;
    private final List<carrierItem> items;
    private final OnItemClickListener listener;

    public carrierAdapter(Context context, List<carrierItem> items, OnItemClickListener listener) {
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
        carrierItem item = items.get(position);

        // מציג מספר לפי המיקום (מתחיל מ-1)
        holder.positionNumber.setText(String.valueOf(position + 1));
        holder.carrier.setText(item.getCarrier());

        // כפתור עריכה
        holder.btnSave.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(holder.getAdapterPosition(), item);
            }
        });

        // כפתור מחיקה
        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                items.remove(pos);
                notifyItemRemoved(pos);
                // עדכון המספרים של כל שאר הפריטים
                notifyItemRangeChanged(pos, items.size() - pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // פונקציה להחלפת מיקומים ברשימה (drag & drop)
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < items.size() && toPosition < items.size()) {
            Collections.swap(items, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);

            // עדכון המספרים בטווח המושפע
            int start = Math.min(fromPosition, toPosition);
            int end = Math.max(fromPosition, toPosition);
            notifyItemRangeChanged(start, end - start + 1);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView carrier, positionNumber;
        ImageButton btnSave, deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            positionNumber = itemView.findViewById(R.id.positionNumber);
            carrier = itemView.findViewById(R.id.textView);
            btnSave = itemView.findViewById(R.id.btnSave);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
