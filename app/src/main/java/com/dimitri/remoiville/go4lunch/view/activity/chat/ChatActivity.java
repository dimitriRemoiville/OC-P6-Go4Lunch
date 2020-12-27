package com.dimitri.remoiville.go4lunch.view.activity.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.databinding.ActivityChatBinding;
import com.dimitri.remoiville.go4lunch.model.Message;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.source.repository.MessageFirestoreRepository;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.Query;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ActivityChatBinding mBinding;
    private ChatActivityRecyclerViewAdapter mChatActivityRecyclerViewAdapter;
    private final MessageFirestoreRepository mMessageFirestoreRepository = new MessageFirestoreRepository();
    private User mCurrentUser;
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        mRecyclerView = mBinding.messageRecyclerView;
        mCurrentUser = SingletonCurrentUser.getInstance().getCurrentUser();

        onClickSendMessage();

        //Configure Adapter & RecyclerView
        this.mChatActivityRecyclerViewAdapter = new ChatActivityRecyclerViewAdapter(generateOptionsForAdapter(mMessageFirestoreRepository.getLastMessages()));
        mChatActivityRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mChatActivityRecyclerViewAdapter.getItemCount());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(this.mChatActivityRecyclerViewAdapter);
    }

    private void onClickSendMessage() {
        mBinding.messageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mBinding.messageEditText.getText())) {
                    // SEND A TEXT MESSAGE
                    mMessageFirestoreRepository.createMessageForChat(mBinding.messageEditText.getText().toString(), mCurrentUser)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                    mBinding.messageEditText.setText("");
                }
            }
        });
    }

    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }
}