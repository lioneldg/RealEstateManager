package com.openclassrooms.realestatemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.openclassrooms.realestatemanager.model.Estate;
import java.util.ArrayList;

public class EstateRVAdapter extends RecyclerView.Adapter<EstateRVAdapter.MyViewHolder> {
    @NonNull
    private final ArrayList<Estate> estates;
    EstateRVFragment estateRVFragment;

    public EstateRVAdapter(@NonNull ArrayList<Estate> estates, EstateRVFragment estateRVFragment) {
        this.estates = estates;
        this.estateRVFragment = estateRVFragment;
    }

    public int getItemCount() {
        return estates.size();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull EstateRVAdapter.MyViewHolder holder, int position) {
        Estate estate = estates.get(position);
        holder.display(estate, position, estateRVFragment);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView estateType;
        private final TextView estateCity;
        private final TextView estatePrice;
        private int bindPosition;
        private EstateRVFragment estateRVFragment;

        public MyViewHolder(final View itemView) {
            super(itemView);
            estateType = itemView.findViewById(R.id.cellTextType);
            estateCity = itemView.findViewById(R.id.cellTextLocation);
            estatePrice = itemView.findViewById(R.id.cellTextPrice);
            itemView.setOnClickListener(view -> estateRVFragment.setCurrentDetailView(bindPosition));

        }

        public void display(Estate estate, int bindPosition, EstateRVFragment estateRVFragment) {
            this.estateRVFragment = estateRVFragment;
            this.bindPosition = bindPosition;
            estateType.setText(estate.getEstateType());
            estateCity.setText(estate.getEstateCity());
            estatePrice.setText(String.format("$%s", estate.getEstatePrice()));
        }
    }
}
