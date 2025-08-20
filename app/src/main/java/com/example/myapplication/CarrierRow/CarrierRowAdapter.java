package com.example.myapplication.CarrierRow;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class CarrierRowAdapter extends RecyclerView.Adapter<CarrierRowAdapter.ItemViewHolder> {

    private final Context context;
    private final List<CarrierRowItem> items;

    public CarrierRowAdapter(Context context, List<CarrierRowItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.carrier_row_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CarrierRowItem item = items.get(position);
        holder.subTopic.setText(item.getSubTopic());

        holder.menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);

            // יצירת פריטים
            MenuItem editItem = popup.getMenu().add("Edit");
            editItem.setIcon(R.drawable.ic_edit);
            MenuItem deleteItem = popup.getMenu().add("Delete");
            deleteItem.setIcon(R.drawable.ic_delete);

            // הצגת האייקונים
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // עיצוב טקסט
            SpannableString editText = new SpannableString("Edit");
            editText.setSpan(new ForegroundColorSpan(Color.WHITE), 0, editText.length(), 0);
            editText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, editText.length(), 0);
            editItem.setTitle(editText);

            SpannableString deleteText = new SpannableString("Delete");
            deleteText.setSpan(new ForegroundColorSpan(Color.WHITE), 0, deleteText.length(), 0);
            deleteText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, deleteText.length(), 0);
            deleteItem.setTitle(deleteText);

            popup.setOnMenuItemClickListener(menuItem -> {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return false;

                switch (menuItem.getTitle().toString()) {
                    case "Edit":
                        // קוד לעריכה
                        return true;
                    case "Delete":
                        items.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, items.size());
                        return true;
                }
                return false;
            });

            popup.show();
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CarrierRowItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView subTopic;
        ImageButton menuButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            subTopic = itemView.findViewById(R.id.subTopicText);
            menuButton = itemView.findViewById(R.id.menuButton);
        }
    }
}
