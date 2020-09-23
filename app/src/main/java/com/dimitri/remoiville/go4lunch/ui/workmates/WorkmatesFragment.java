package com.dimitri.remoiville.go4lunch.ui.workmates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.model.Workmate;
import com.dimitri.remoiville.go4lunch.ui.listview.ListViewRecyclerViewAdapter;

import java.util.List;

public class WorkmatesFragment extends Fragment {
    private WorkmatesViewModel mWorkmatesViewModel;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<Workmate> mWorkmates;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mWorkmatesViewModel =
                ViewModelProviders.of(this).get(WorkmatesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workmates_list, container, false);

        mContext = root.getContext();
        mRecyclerView = (RecyclerView) root;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));

        return root;
    }

    private void initList(List<Workmate> workmates) {
        mRecyclerView.setAdapter(new WorkmatesRecyclerViewAdapter(workmates));
    }
}
