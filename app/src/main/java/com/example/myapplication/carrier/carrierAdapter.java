package com.example.myapplication.carrier;

import android.content.Intent;
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
import com.example.myapplication.New_problem;
import com.example.myapplication.Problems;
import com.example.myapplication.R;

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

        // מספר שורה מעודכן
        holder.positionNumber.setText("" + (position + 1));

        // אתחול Inner RecyclerView
        if (holder.innerAdapter == null) {
            holder.innerAdapter = new CarrierRowAdapter(item.getInnerItems(), fragment, position);
            holder.recyclerViewInner.setLayoutManager(new LinearLayoutManager(fragment.requireContext()));
            holder.recyclerViewInner.setAdapter(holder.innerAdapter);
            holder.innerAdapter.getItemTouchHelper().attachToRecyclerView(holder.recyclerViewInner);
        } else {
            holder.innerAdapter.updateItems(item.getInnerItems());
        }

        // קבע visibility לפי המודל
        holder.recyclerViewInner.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);

        // לחיצה על carrier תפתח/תסגור את הפנימי
        holder.carrier.setOnClickListener(v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(position); // ריענון השורה עם הסטטוס החדש
        });

        // כפתור הוספת פריט
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.requireContext(), New_problem.class);
                intent.putExtra("position", position);
                intent.putExtra("carrier", item.getCarrierName());
                onAddNewItemClick.accept(position);
            });
        }

        // כפתור עריכה
        holder.btnEdit.setOnClickListener(v -> {
            if (fragment instanceof Problems) {
                Intent intent = new Intent(fragment.requireContext(), EditCarrier.class);
                intent.putExtra("position", position);
                intent.putExtra("carrier", item.getCarrierName());
                ((Problems) fragment).launchEditCarrier(intent);
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

    public void moveItem(int fromPosition, int toPosition) {
        carrierItem movedItem = items.remove(fromPosition);
        items.add(toPosition, movedItem);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(Math.min(fromPosition, toPosition), items.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView carrier, positionNumber;
        RecyclerView recyclerViewInner;
        ImageButton btnAdd, btnEdit, deleteButton;
        CarrierRowAdapter innerAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carrier = itemView.findViewById(R.id.textView);
            positionNumber = itemView.findViewById(R.id.positionNumber);
            recyclerViewInner = itemView.findViewById(R.id.recyclerViewInner);
            btnAdd = itemView.findViewById(R.id.addButton);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            innerAdapter = null;
        }
    }
}
