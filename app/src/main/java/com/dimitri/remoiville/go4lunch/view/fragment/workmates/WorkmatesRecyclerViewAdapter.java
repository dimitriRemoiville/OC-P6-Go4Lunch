package com.dimitri.remoiville.go4lunch.view.fragment.workmates;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.FragmentWorkmatesBinding;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.view.activity.DetailsPlaceActivity;

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
        final User user = mWorkmates.get(position);
        Glide.with(holder.itemView)
                .load(user.getURLProfilePicture())
                .circleCrop()
                .into(holder.mFragmentWorkmatesBinding.listWorkmatesImg);
        String workmateTxt = user.getFirstName();
        if (user.getRestaurantID() != null) {
            workmateTxt = workmateTxt + " is eating at " + user.getRestaurantName();
        } else {
            workmateTxt = workmateTxt + " hasn't decided yet";
            int grey = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorTextGrey);
            holder.mFragmentWorkmatesBinding.listWorkmatesTxt.setTextColor(grey);
        }
        holder.mFragmentWorkmatesBinding.listWorkmatesTxt.setText(workmateTxt);

        holder.itemView.setOnClickListener(v -> {
            if (mWorkmates.get(position).getRestaurantID() != null) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailsPlaceActivity.class);
                intent.putExtra("placeId", mWorkmates.get(position).getRestaurantID());
                holder.itemView.getContext().startActivity(intent);
            }
        });
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
