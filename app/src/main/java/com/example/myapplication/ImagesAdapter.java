package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {

    private final Context context;
    private final List<Uri> images;

    public ImagesAdapter(Context context, List<Uri> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutRes;

        // בדיקה אוטומטית לפי סוג ה-Context
        if (context instanceof New_problem) {
            layoutRes = R.layout.item_image; // דף X
        } else {
            layoutRes = R.layout.image_row; // דף Y
        }
        View view = LayoutInflater.from(context).inflate(layoutRes, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = images.get(position);

        try {
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                drawable = android.graphics.ImageDecoder.decodeDrawable(
                        android.graphics.ImageDecoder.createSource(context.getContentResolver(), imageUri)
                );
            } else {
                drawable = Drawable.createFromStream(
                        context.getContentResolver().openInputStream(imageUri),
                        imageUri.toString()
                );
            }
            holder.imageView.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.ic_close_white); // תמונה ברירת מחדל במקרה של שגיאה
        }

        // כפתור מחיקה
        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                images.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, images.size());

                if (context instanceof New_problem) {
                    ((New_problem) context).updateRecyclerViewImagesVisibility();
                }
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            try {
                Intent editIntent = new Intent(Intent.ACTION_VIEW);
                editIntent.setDataAndType(imageUri, "image/*");
                editIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // מנסה לפתוח עם גלריה של סמסונג
                editIntent.setPackage("com.sec.android.gallery3d");

                context.startActivity(editIntent);

            } catch (Exception e) {
                e.printStackTrace();
                // fallback - פותח כל עורך זמין
                try {
                    Intent fallbackIntent = new Intent(Intent.ACTION_EDIT);
                    fallbackIntent.setDataAndType(imageUri, "image/*");
                    fallbackIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(fallbackIntent, "ערוך תמונה"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, "לא נמצאה אפליקציית עריכה מתאימה", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton btnDelete;
        ImageButton btnEdit;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageItem);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
            btnEdit = itemView.findViewById(R.id.btnEditItem);
        }
    }
}
