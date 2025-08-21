// CarrierRowAdapter.java
package com.example.myapplication.CarrierRow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;

public class CarrierRowAdapter extends RecyclerView.Adapter<CarrierRowAdapter.ViewHolder> {

    private ArrayList<CarrierRowItem> innerItems;
    private Context context;

    public CarrierRowAdapter(ArrayList<CarrierRowItem> innerItems) {
        this.innerItems = innerItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carrier_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierRowItem item = innerItems.get(position);

        Log.d("CarrierRowAdapter", "Position: " + position + " SubTopic: " + item.getSubTopic());

        holder.txtSubTopic.setText(item.getSubTopic());
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

    // הזזת פריט בתוככי ה-RecyclerView
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= innerItems.size() || toPosition >= innerItems.size())
            return;

        Collections.swap(innerItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    // הפעלת ItemTouchHelper ל-Drag & Drop פנימי
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubTopic = itemView.findViewById(R.id.txtSubTopic);
        }
    }
}
