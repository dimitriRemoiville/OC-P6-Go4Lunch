package com.dimitri.remoiville.go4lunch.view.fragment.workmates;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.databinding.FragmentWorkmatesBinding;
import com.dimitri.remoiville.go4lunch.model.User;

import java.util.List;

public class WorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesRecyclerViewAdapter.ViewHolder> {

    private final List<User> mWorkmates;

    public WorkmatesRecyclerViewAdapter(List<User> items) {
        mWorkmates = items;
    }

    @NonNull
    @Override
    public WorkmatesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentWorkmatesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesRecyclerViewAdapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mWorkmates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentWorkmatesBinding mFragmentWorkmatesBinding;

        public ViewHolder(FragmentWorkmatesBinding fragmentWorkmatesBinding) {
            super(fragmentWorkmatesBinding.getRoot());
            this.mFragmentWorkmatesBinding = fragmentWorkmatesBinding;
        }
    }
}
