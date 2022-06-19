package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.openclassrooms.realestatemanager.model.Estate;
import java.util.ArrayList;

public class MasterRVAdapter extends RecyclerView.Adapter<MasterRVAdapter.MyViewHolder> {
    @NonNull
    private final ArrayList<Estate> estates;
    MasterRVFragment masterRVFragment;

    public MasterRVAdapter(@NonNull ArrayList<Estate> estates, MasterRVFragment masterRVFragment) {
        this.estates = estates;
        this.masterRVFragment = masterRVFragment;
    }

    public int getItemCount() {
        return estates.size();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.master_list_cell, parent, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MasterRVAdapter.MyViewHolder holder, int position) {
        Estate estate = estates.get(position);
        holder.display(estate, masterRVFragment);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView estateImage;
        private final TextView estateType;
        private final TextView estateNeighborhood;
        private final TextView estatePrice;
        private int bindPosition;
        private MasterRVFragment masterRVFragment;

        public MyViewHolder(final View itemView) {
            super(itemView);
            estateImage = itemView.findViewById(R.id.cellImage);
            estateType = itemView.findViewById(R.id.cellTextType);
            estateNeighborhood = itemView.findViewById(R.id.cellTextLocation);
            estatePrice = itemView.findViewById(R.id.cellTextPrice);
            itemView.setOnClickListener(view -> masterRVFragment.setCurrentDetailView(bindPosition));
        }

        public void display(Estate estate, MasterRVFragment masterRVFragment) {
            Context context = masterRVFragment.requireActivity().getApplicationContext();
            this.masterRVFragment = masterRVFragment;
            int detailPosition = masterRVFragment.getBindPosition();
            bindPosition = (int) estate.getId() - 1;
            if(detailPosition == bindPosition && Utils.isTablet(context) && Utils.isLandscapeOrientation(context)){
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.colorSelection));
            }
            if(estate.getPhotosListString() != null) {
                ArrayList<String> photos = Utils.fromStringListToArrayList(estate.getPhotosListString());
                Bitmap photo = Utils.getBitmapFromFileName(photos.get(0), 150, context);
                estateImage.setImageBitmap(photo);
            }
            estateType.setText(estate.getEstateType());
            estateNeighborhood.setText(estate.getEstateCity());
            estatePrice.setText(String.format("$%s", estate.getEstatePrice()));
        }
    }
}
