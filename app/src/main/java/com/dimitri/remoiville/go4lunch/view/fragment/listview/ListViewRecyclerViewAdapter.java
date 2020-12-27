package com.dimitri.remoiville.go4lunch.view.fragment.listview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.FragmentListviewBinding;
import com.dimitri.remoiville.go4lunch.model.PlaceRestaurant;
import com.dimitri.remoiville.go4lunch.view.activity.DetailsPlaceActivity;

import java.util.List;

public class ListViewRecyclerViewAdapter  extends RecyclerView.Adapter<ListViewRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceRestaurant> mPlaceRestaurants;
    private final Bitmap mBitmap;
    private static final String TAG = "ListViewRecyclerViewAda";

    public ListViewRecyclerViewAdapter(List<PlaceRestaurant> items) {
        mPlaceRestaurants = items;
        mBitmap = null;
    }

    public ListViewRecyclerViewAdapter(List<PlaceRestaurant> items, Bitmap bitmap) {
        mPlaceRestaurants = items;
        mBitmap = bitmap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentListviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PlaceRestaurant place = mPlaceRestaurants.get(position);
        holder.mFragmentListviewBinding.listPlacesName.setText(place.getName());
        String distance = place.getDistance() + "m";
        holder.mFragmentListviewBinding.listPlacesDistance.setText(distance);
        holder.mFragmentListviewBinding.listPlacesTypeAddress.setText(place.getAddress());
        int nbWorkmates = place.getUserList().size();
        String nbWorkmatesString = "" + nbWorkmates;
        holder.mFragmentListviewBinding.listPlacesNbWorkmates.setText(nbWorkmatesString);
        if (place.isOpen()) {
            holder.mFragmentListviewBinding.listPlacesOpeningHours.setText(R.string.listView_restaurant_open);
        } else {
            holder.mFragmentListviewBinding.listPlacesOpeningHours.setText(R.string.listView_restaurant_close);
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
        if (mBitmap == null) {
            Glide.with(holder.itemView)
                    .load(place.getUrlPicture())
                    .into(holder.mFragmentListviewBinding.listPlacesImg);
        } else {
            Glide.with(holder.itemView)
                    .load(mBitmap)
                    .into(holder.mFragmentListviewBinding.listPlacesImg);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailsPlaceActivity.class);
            intent.putExtra("placeId", mPlaceRestaurants.get(position).getPlaceId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mPlaceRestaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentListviewBinding mFragmentListviewBinding;

        public ViewHolder(FragmentListviewBinding fragmentListviewBinding) {
            super(fragmentListviewBinding.getRoot());
            this.mFragmentListviewBinding = fragmentListviewBinding;
        }
    }

}
