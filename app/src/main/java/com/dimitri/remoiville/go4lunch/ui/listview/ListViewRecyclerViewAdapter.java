package com.dimitri.remoiville.go4lunch.ui.listview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.databinding.FragmentListviewBinding;
import com.dimitri.remoiville.go4lunch.model.Place;

import java.util.List;

public class ListViewRecyclerViewAdapter  extends RecyclerView.Adapter<ListViewRecyclerViewAdapter.ViewHolder> {

    private List<Place> mPlaces;

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
        // TODO
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
