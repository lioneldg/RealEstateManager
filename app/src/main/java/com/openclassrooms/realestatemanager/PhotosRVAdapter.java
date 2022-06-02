package com.openclassrooms.realestatemanager;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhotosRVAdapter extends RecyclerView.Adapter<PhotosRVAdapter.MyViewHolder> {

    private final ArrayList<String> photos;
    private final EditEstateActivity editEstateActivity;
    private final boolean isSold;

    public PhotosRVAdapter(ArrayList<String> photos, EditEstateActivity editEstateActivity, boolean isSold) {
        this.photos = photos;
        this.editEstateActivity = editEstateActivity;
        this.isSold = isSold;
    }


    public int getItemCount() {
        return photos.size();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.photos_list_cell, parent, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull PhotosRVAdapter.MyViewHolder holder, int position) {
        String photoName = photos.get(position);
        holder.display(photoName, editEstateActivity, isSold);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private EditEstateActivity editEstateActivity;
        private String photoName;
        private final ImageView estateImage;
        private final ImageButton deleteButton;
        private final TextView soldText;

        public MyViewHolder(final View itemView) {
            super(itemView);
            estateImage = itemView.findViewById(R.id.detailPhotoCell);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            soldText = itemView.findViewById(R.id.soldText);
            deleteButton.setOnClickListener(view -> {
                if(editEstateActivity != null) {
                    ArrayList<String> photos = editEstateActivity.getPhotos();
                    photos.remove(photoName);
                    editEstateActivity.setPhotos(photos);
                    editEstateActivity.setAdapter();
                }
            });
        }

        public void display(String photoName, EditEstateActivity editEstateActivity, boolean isSold) {
            this.editEstateActivity = editEstateActivity;
            deleteButton.setVisibility(editEstateActivity != null ? View.VISIBLE : View.INVISIBLE);
            soldText.setVisibility(isSold ? View.VISIBLE : View.INVISIBLE);
            this.photoName = photoName;
            Bitmap photo = Utils.getBitmapFromFileName(photoName, 350, itemView.getContext());
            estateImage.setImageBitmap(photo);
        }
    }
}
