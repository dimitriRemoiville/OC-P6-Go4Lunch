package com.dimitri.remoiville.go4lunch.ui.listview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.databinding.FragmentListviewBinding;
import com.dimitri.remoiville.go4lunch.model.Place;

import java.util.List;

public class ListViewRecyclerViewAdapter  extends RecyclerView.Adapter<ListViewRecyclerViewAdapter.ViewHolder> {

    private List<Place> mPlaces;
    private static final String TAG = "ListViewRecyclerViewAda";

    public ListViewRecyclerViewAdapter(List<Place> items) {
        mPlaces = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentListviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ici");
        final Place place = mPlaces.get(position);
        holder.mFragmentListviewBinding.listPlacesName.setText(place.getName());
        holder.mFragmentListviewBinding.listPlacesDistance.setText("100m");
        holder.mFragmentListviewBinding.listPlacesTypeAddress.setText(place.getAddress());
        int nbWorkmates = place.getWorkmateList().size();
        String nbWorkmatesString = "" + nbWorkmates;
        holder.mFragmentListviewBinding.listPlacesNbWorkmates.setText(nbWorkmatesString);
        if (place.isOpen()) {
            holder.mFragmentListviewBinding.listPlacesOpeningHours.setText("Open");
        } else {
            holder.mFragmentListviewBinding.listPlacesOpeningHours.setText("Closed");
        }
        holder.mFragmentListviewBinding.listPlacesStar1.setVisibility(View.INVISIBLE);
        holder.mFragmentListviewBinding.listPlacesStar2.setVisibility(View.INVISIBLE);
        holder.mFragmentListviewBinding.listPlacesStar3.setVisibility(View.INVISIBLE);
        switch (place.getRating()) {
            case 1:
                holder.mFragmentListviewBinding.listPlacesStar3.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.mFragmentListviewBinding.listPlacesStar2.setVisibility(View.VISIBLE);
                holder.mFragmentListviewBinding.listPlacesStar3.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.mFragmentListviewBinding.listPlacesStar1.setVisibility(View.VISIBLE);
                holder.mFragmentListviewBinding.listPlacesStar2.setVisibility(View.VISIBLE);
                holder.mFragmentListviewBinding.listPlacesStar3.setVisibility(View.VISIBLE);
                break;
        }
        Glide.with(holder.itemView)
                .load(place.getUrlPicture())
                .into(holder.mFragmentListviewBinding.listPlacesImg);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private FragmentListviewBinding mFragmentListviewBinding;

        public ViewHolder(FragmentListviewBinding fragmentListviewBinding) {
            super(fragmentListviewBinding.getRoot());
            this.mFragmentListviewBinding = fragmentListviewBinding;
        }
    }

}
