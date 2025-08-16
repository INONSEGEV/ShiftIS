package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class standardAdapter extends RecyclerView.Adapter<standardAdapter.ItemViewHolder> {


    private final Context context;
    private final List<standardItem> items;

    public standardAdapter(Context context, List<standardItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_problems, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        standardItem item = items.get(position);

        if (holder.standard != null) {
            holder.standard.setText(item.getStandard());
        } else {
            Toast.makeText(context, "TextView לא נמצא ב-XML", Toast.LENGTH_SHORT).show();
        }

        if (holder.btnSave != null) {
            // עריכה
            holder.btnSave.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditStandard.class);
                intent.putExtra("standard", item.getStandard());
                intent.putExtra("position", position);

                if (context instanceof New_problem) {
                    ((New_problem) context).startActivityForResult(intent, 2);
                }
            });
        }

        if (holder.deleteButton != null) {
            // מחיקה
            holder.deleteButton.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    items.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, items.size());

                    // הוספתי את זה: בדיקה אם הרשימה ריקה כדי להסתיר את RecyclerView
                    if (context instanceof New_problem) {
                        ((New_problem) context).updateRecyclerViewVisibility();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView standard;
        ImageButton btnSave; // כפתור העריכה
        ImageButton deleteButton;    // כפתור המחיקה

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            standard = itemView.findViewById(R.id.textView);
            btnSave = itemView.findViewById(R.id.btnSave);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
