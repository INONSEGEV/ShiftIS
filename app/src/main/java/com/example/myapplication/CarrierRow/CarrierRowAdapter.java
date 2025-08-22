// CarrierRowAdapter.java
package com.example.myapplication.CarrierRow;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.New_problem;
import com.example.myapplication.Problems;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;

public class CarrierRowAdapter extends RecyclerView.Adapter<CarrierRowAdapter.ViewHolder> {

    private ArrayList<CarrierRowItem> innerItems;
    private Fragment fragment;

    public CarrierRowAdapter(ArrayList<CarrierRowItem> innerItems, Fragment fragment) {
        this.innerItems = innerItems;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.carrier_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int parentPosition) {
        CarrierRowItem item = innerItems.get(parentPosition);

        holder.txtSubTopic.setText(item.getSubTopic());

        if (holder.btnEdit != null) {
            holder.btnEdit.setOnClickListener(v -> {
                // מעבר ל-New_problem עם Intent
                Intent intent = new Intent(fragment.getContext(), New_problem.class);

                intent.putExtra("carrier", item.getCarrier());
                intent.putExtra("subTopic", item.getSubTopic());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("remark", item.getRemark());
                intent.putExtra("date", item.getDate());
                intent.putParcelableArrayListExtra("selectedImages", item.getImages());
                intent.putParcelableArrayListExtra("standardItems", item.getStandard());
                intent.putParcelableArrayListExtra("recommendationsItems", item.getRecommendations());
                intent.putExtra("parentPosition", parentPosition);

                if (fragment instanceof Problems) {
                    ((Problems) fragment).startActivityForResult(intent, 4);
                } else {
                    fragment.startActivity(intent);
                }
            });
        }

        if (holder.btnDelete != null) {
            holder.btnDelete.setOnClickListener(v -> {
                innerItems.remove(parentPosition);
                notifyItemRemoved(parentPosition);
                notifyItemRangeChanged(parentPosition, innerItems.size());
            });
        }
    }

    @Override
    public int getItemCount() {
        return innerItems.size();
    }

    public void addItem(CarrierRowItem newItem) {
        innerItems.add(newItem);
        notifyItemInserted(innerItems.size() - 1);
    }

    public void updateItems(ArrayList<CarrierRowItem> newItems) {
        this.innerItems = newItems;
        notifyDataSetChanged();
    }

    // הזזת פריט בתוך ה-RecyclerView
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= innerItems.size() || toPosition >= innerItems.size())
            return;

        Collections.swap(innerItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    // הפעלת ItemTouchHelper ל-Drag & Drop
    public ItemTouchHelper getItemTouchHelper() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                moveItem(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // אין סווייפ למחיקה
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true; // מאפשר גרירה בלחיצה ארוכה
            }
        };

        return new ItemTouchHelper(callback);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubTopic;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubTopic = itemView.findViewById(R.id.txtSubTopic);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.deleteButton);
        }
    }
    public void updateItem(int position, CarrierRowItem updatedItem) {
        if (position < 0 || position >= innerItems.size()) return;
        innerItems.set(position, updatedItem);
        notifyItemChanged(position);
    }

}
