package com.example.myapplication.CarrierRow;

import android.content.Context;
import android.content.Intent;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierRowItem item = innerItems.get(position);

        holder.txtSubTopic.setText(item.getSubTopic());
        holder.positionNumber.setText("" + (position + 1)); // מספר השורה

        // כפתור עריכה
        if (holder.btnEdit != null) {
            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getContext(), New_problem.class);
                intent.putExtra("carrier", item.getCarrier());
                intent.putExtra("subTopic", item.getSubTopic());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("remark", item.getRemark());
                intent.putExtra("date", item.getDate());
                intent.putParcelableArrayListExtra("selectedImages", item.getImages());
                intent.putParcelableArrayListExtra("standardItems", item.getStandard());
                intent.putParcelableArrayListExtra("recommendationsItems", item.getRecommendations());
                intent.putExtra("parentPosition", position);

                if (fragment instanceof Problems) {
                    ((Problems) fragment).startActivityForResult(intent, 4);
                } else {
                    fragment.startActivity(intent);
                }
            });
        }

        // כפתור מחיקה
        if (holder.btnDelete != null) {
            holder.btnDelete.setOnClickListener(v -> {
                innerItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, innerItems.size()); // עדכון מספרי שורות מתחת
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

    // הזזת פריט בתוך ה-RecyclerView הפנימי
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= innerItems.size() || toPosition >= innerItems.size())
            return;

        Collections.swap(innerItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        // עדכון מספרי שורות של כל הפריטים שנפגעו
        notifyItemRangeChanged(Math.min(fromPosition, toPosition),
                Math.abs(fromPosition - toPosition) + 1);
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
                return true;
            }
        };
        return new ItemTouchHelper(callback);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubTopic;
        TextView positionNumber; // מספר השורה
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubTopic = itemView.findViewById(R.id.txtSubTopic);
            positionNumber = itemView.findViewById(R.id.positionNumber);
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
