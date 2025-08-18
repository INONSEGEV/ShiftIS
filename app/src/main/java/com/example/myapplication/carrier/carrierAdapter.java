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
                notifyItemRangeChanged(pos, items.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView carrier;
        ImageButton btnSave;
        ImageButton deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            carrier = itemView.findViewById(R.id.textView);
            btnSave = itemView.findViewById(R.id.btnSave);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
