// carrierAdapter.java
package com.example.myapplication.carrier;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CarrierRow.CarrierRowAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Problems;

import java.util.ArrayList;
import java.util.function.IntConsumer;

public class carrierAdapter extends RecyclerView.Adapter<carrierAdapter.ViewHolder> {

    private final Fragment fragment;
    private final ArrayList<carrierItem> items;
    private final IntConsumer onAddNewItemClick;

    public carrierAdapter(Fragment fragment, ArrayList<carrierItem> items, IntConsumer onAddNewItemClick) {
        this.fragment = fragment;
        this.items = items;
        this.onAddNewItemClick = onAddNewItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carrier_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        carrierItem item = items.get(position);
        holder.carrier.setText(item.getCarrierName());

        // אתחול Inner RecyclerView
        if (holder.innerAdapter == null) {
            holder.innerAdapter = new CarrierRowAdapter(item.getInnerItems(), fragment);
            holder.recyclerViewInner.setLayoutManager(new LinearLayoutManager(fragment.requireContext()));
            holder.recyclerViewInner.setAdapter(holder.innerAdapter);

            // חיבור ItemTouchHelper פנימי
            holder.innerAdapter.getItemTouchHelper().attachToRecyclerView(holder.recyclerViewInner);
        } else {
            holder.innerAdapter.updateItems(item.getInnerItems());
        }

        // עדכון ה-visibility של ה-RecyclerView הפנימי
        updateRecyclerViewInnerVisibility(holder);

        // לחיצה על carrier תפתח/תסגור את הפנימי
        holder.carrier.setOnClickListener(v -> {
            if (holder.recyclerViewInner.getVisibility() == View.VISIBLE) {
                holder.recyclerViewInner.setVisibility(View.GONE);
            } else if (holder.innerAdapter.getItemCount() > 0) {
                holder.recyclerViewInner.setVisibility(View.VISIBLE);
            }
        });

        // כפתור הוספת פריט
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.btnAdd.setOnClickListener(v -> {
                onAddNewItemClick.accept(position);
                updateRecyclerViewInnerVisibility(holder);
            });
        }

        // כפתור עריכה
        holder.btnEdit.setOnClickListener(v -> {
            if (fragment instanceof Problems) {
                ((Problems) fragment).launchEditCarrier(item.getEditIntent(fragment.requireContext(), position));
            }
        });

        // כפתור מחיקה
        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                items.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // פונקציה להזזה (Drag & Drop למשל)
    public void moveItem(int fromPosition, int toPosition) {
        carrierItem movedItem = items.remove(fromPosition);
        items.add(toPosition, movedItem);
        notifyItemMoved(fromPosition, toPosition);
    }

    // פונקציה שמסתירה/מציגה את ה-RecyclerView הפנימי
    private void updateRecyclerViewInnerVisibility(ViewHolder holder) {
        if (holder.innerAdapter != null && holder.innerAdapter.getItemCount() > 0) {
            holder.recyclerViewInner.setVisibility(View.VISIBLE);
        } else {
            holder.recyclerViewInner.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView carrier;
        RecyclerView recyclerViewInner;
        ImageButton btnAdd, btnEdit, deleteButton;
        CarrierRowAdapter innerAdapter;
  
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carrier = itemView.findViewById(R.id.textView);
            recyclerViewInner = itemView.findViewById(R.id.recyclerViewInner);
            btnAdd = itemView.findViewById(R.id.addButton);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            innerAdapter = null;
        }
    }
}
