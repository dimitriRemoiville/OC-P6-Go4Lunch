package com.dimitri.remoiville.go4lunch.view.activity.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.databinding.ItemMessageReceivedBinding;
import com.dimitri.remoiville.go4lunch.databinding.ItemMessageSentBinding;
import com.dimitri.remoiville.go4lunch.model.Message;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatActivityRecyclerViewAdapter extends FirestoreRecyclerAdapter<Message, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private final User mCurrentUser;


    public ChatActivityRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
        mCurrentUser = SingletonCurrentUser.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            return new SentMessageHolder(ItemMessageSentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            return new ReceivedMessageHolder(ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        return null;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Message model) {
        if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT) {
            ItemMessageSentBinding binding = ((SentMessageHolder) holder).mSentBinding;
            binding.textMessageBody.setText(model.getText());
        } else if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED) {
            ItemMessageReceivedBinding binding = ((ReceivedMessageHolder) holder).mReceivedBinding;
            String fullName = model.getSendBy().getFirstName() + " " + model.getSendBy().getLastName();
            binding.textMessageName.setText(fullName);
            binding.textMessageBody.setText(model.getText());

            Glide.with(holder.itemView)
                    .load(model.getSendBy().getURLProfilePicture())
                    .circleCrop()
                    .into(binding.imageMessageProfile);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCurrentUser.getUserID().equals(getItem(position).getSendBy().getUserID())) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public static class SentMessageHolder extends RecyclerView.ViewHolder {

        private final ItemMessageSentBinding mSentBinding;

        public SentMessageHolder(ItemMessageSentBinding itemMessageSentBinding) {
            super(itemMessageSentBinding.getRoot());
            this.mSentBinding = itemMessageSentBinding;
        }
    }

    public static class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        private final ItemMessageReceivedBinding mReceivedBinding;

        public ReceivedMessageHolder(ItemMessageReceivedBinding itemMessageReceivedBinding) {
            super(itemMessageReceivedBinding.getRoot());
            this.mReceivedBinding = itemMessageReceivedBinding;
        }
    }
}