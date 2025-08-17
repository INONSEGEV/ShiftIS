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

        holder.standard.setText(item.getStandard());

        // כפתור עריכה
        holder.btnSave.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditStandard.class);
            intent.putExtra("standard", item.getStandard());
            intent.putExtra("position", position);
            intent.putParcelableArrayListExtra("selectedImages", item.getImages()); // מעביר תמונות

            if (context instanceof New_problem) {
                ((New_problem) context).startActivityForResult(intent, New_problem.EDIT_ITEM_REQUEST);
            }
        });

        // כפתור מחיקה
        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                items.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, items.size());

                if (context instanceof New_problem) {
                    ((New_problem) context).updateRecyclerViewVisibility();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView standard;
        ImageButton btnSave;
        ImageButton deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            standard = itemView.findViewById(R.id.textView);
            btnSave = itemView.findViewById(R.id.btnSave);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
