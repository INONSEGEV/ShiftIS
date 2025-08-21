package com.example.myapplication.CarrierRow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class CarrierRowAdapter extends RecyclerView.Adapter<CarrierRowAdapter.ViewHolder> {

    private final ArrayList<CarrierRowItem> innerItems;

    public CarrierRowAdapter(ArrayList<CarrierRowItem> innerItems) {
        this.innerItems = innerItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carrier_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierRowItem item = innerItems.get(position);
        holder.txtSubTopic.setText(item.getSubTopic());
    }

    @Override
    public int getItemCount() {
        return innerItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubTopic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubTopic = itemView.findViewById(R.id.txtSubTopic);

        }
    }
}
