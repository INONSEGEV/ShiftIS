package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ProblemItem> itemList;

    public MyAdapter(List<ProblemItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_problems, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProblemItem item = itemList.get(position);

        // הכותרת הראשית
        holder.textView.setText(item.getTitle());

        // פרטים נוספים
        holder.subTopicText.setText("תת-נושא: " + (item.getSubTopic() != null ? item.getSubTopic() : ""));
        holder.descriptionText.setText("תיאור: " + (item.getDescription() != null ? item.getDescription() : ""));
        holder.remarkText.setText("הערה: " + (item.getRemark() != null ? item.getRemark() : ""));
        holder.dateText.setText("תאריך: " + (item.getDate() != null ? item.getDate() : ""));

        // מצב ראשוני של פרטי השורה
        holder.detailsLayout.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);

        // לחיצה על השורה הראשית כדי לפתוח/לסגור את הפרטים
        holder.textView.setOnClickListener(v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(position);
        });

        // כפתור מחיקה
        holder.deleteButton.setOnClickListener(v -> {
            itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, itemList.size());
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, subTopicText, descriptionText, remarkText, dateText;
        ImageButton deleteButton;
        LinearLayout detailsLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            subTopicText = itemView.findViewById(R.id.subTopicText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            remarkText = itemView.findViewById(R.id.remarkText);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
}
