package com.dimitri.remoiville.go4lunch.ui.listview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.model.Place;

import java.util.List;

public class ListViewFragment extends Fragment {

    private ListViewViewModel mListViewViewModel;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<Place> mPlaces;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mListViewViewModel =
                ViewModelProviders.of(this).get(ListViewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listview_list, container, false);

        mContext = root.getContext();
        mRecyclerView = (RecyclerView) root;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        return root;
    }

    private void initList(List<Place> places) {
        mRecyclerView.setAdapter(new ListViewRecyclerViewAdapter(places));
    }
}
