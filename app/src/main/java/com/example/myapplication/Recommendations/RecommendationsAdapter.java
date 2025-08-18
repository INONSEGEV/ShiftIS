package com.example.myapplication.Recommendations;

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

import com.example.myapplication.New_problem;
import com.example.myapplication.R;

import java.util.List;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ItemViewHolder> {

    private final Context context;
    private final List<RecommendationsItem> items;

    public RecommendationsAdapter(Context context, List<RecommendationsItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecommendationsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_problems, parent, false);
        return new RecommendationsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationsAdapter.ItemViewHolder holder, int position1) {
        RecommendationsItem item = items.get(position1);

        if (holder.description != null) {
            holder.description.setText(item.getDescription());
        } else {
            Toast.makeText(context, "TextView לא נמצא ב-XML", Toast.LENGTH_SHORT).show();
        }

        if (holder.btnSave != null) {
            // עריכה
            holder.btnSave.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditItemRecommendations.class);
                intent.putExtra("description", item.getDescription());
                intent.putExtra("amount", item.getAmount());
                intent.putExtra("unitPrice", item.getUnitPrice());
                intent.putExtra("unit", item.getUnit());
                intent.putExtra("totalPrice", item.getTotalPrice());
                intent.putExtra("position1", position1);

                if (context instanceof New_problem) {
                    ((New_problem) context).startActivityForResult(intent, 4);
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

                    // בדיקה אם הרשימה ריקה כדי להסתיר את RecyclerView
                    if (context instanceof New_problem) {
                        ((New_problem) context).updateRecyclerViewRecommendationsVisibility();
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
        TextView description;
        ImageButton btnSave; // כפתור העריכה
        ImageButton deleteButton; // כפתור המחיקה

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.textView);
            btnSave = itemView.findViewById(R.id.btnSave);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
