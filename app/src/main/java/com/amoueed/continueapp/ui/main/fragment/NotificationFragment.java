package com.amoueed.continueapp.ui.main.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoueed.continueapp.R;
import com.amoueed.continueapp.adapter.WeekAdapter;
import com.amoueed.continueapp.database.AppDatabase;
import com.amoueed.continueapp.database.AppExecutors;
import com.amoueed.continueapp.database.WeekEntry;
import com.amoueed.continueapp.viewmodel.WeekViewModel;

import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class NotificationFragment extends Fragment implements WeekAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private WeekAdapter mAdapter;
    private AppDatabase mDb;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        mRecyclerView = rootView.findViewById(R.id.week_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new WeekAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity().getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<WeekEntry> tasks = mAdapter.getWeeks();
                        mDb.weekDao().deleteWeek(tasks.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        setupViewModel();
        return rootView;
    }

    private void setupViewModel() {

        WeekViewModel viewModel = ViewModelProviders.of(this).get(WeekViewModel.class);
        viewModel.getWeeks().observe(this, new Observer<List<WeekEntry>>() {
            @Override
            public void onChanged(@Nullable List<WeekEntry> weekEntries) {
                mAdapter.setWeeks(weekEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {

    }
}
