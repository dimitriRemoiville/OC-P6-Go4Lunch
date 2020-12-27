package com.dimitri.remoiville.go4lunch.view.fragment.workmates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;

import java.util.List;

public class WorkmatesFragment extends Fragment {
    private MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private User currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.action_search);
        if(search!=null) {
            search.setVisible(false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workmates_list, container, false);

        mContext = root.getContext();
        mRecyclerView = (RecyclerView) root;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));

        currentUser = SingletonCurrentUser.getInstance().getCurrentUser();

        getWorkmateList();

        return root;
    }

    private void getWorkmateList() {
        mMainViewModel.getAllUsersSortByRestaurantID().observe(getViewLifecycleOwner(), users -> {
            String uid = currentUser.getUserID();
            for(int i = 0; i < users.size(); i++){
                if (users.get(i).getUserID().equals(uid)
                        || users.get(i).getFirstName() == null
                        || users.get(i).getLastName() == null) {
                    users.remove(users.get(i));
                }
            }
            initList(users);
        });
    }

    private void initList(List<User> users) {
        mRecyclerView.setAdapter(new WorkmatesRecyclerViewAdapter(users));
    }
}
