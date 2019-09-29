package com.amoueed.continueapp.ui.main.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.amoueed.continueapp.R;
import com.amoueed.continueapp.adapter.WeekAdapter;
import com.amoueed.continueapp.database.AppDatabase;
import com.amoueed.continueapp.database.AppExecutors;
import com.amoueed.continueapp.database.WeekEntry;
import com.amoueed.continueapp.ui.EnrollmentActivity;
import com.amoueed.continueapp.ui.NotificationDetailActivity;
import com.amoueed.continueapp.ui.SplashScreenActivity;
import com.amoueed.continueapp.ui.main.MainActivity;
import com.amoueed.continueapp.viewmodel.WeekViewModel;

import org.apache.commons.io.FilenameUtils;

import java.util.Date;
import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class NotificationFragment extends Fragment implements WeekAdapter.ItemClickListener {

    private static final String FILE_NAME = "fileName";

    private RecyclerView mRecyclerView;
    private WeekAdapter mAdapter;

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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
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

            }
        }).attachToRecyclerView(mRecyclerView);



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
        Intent in = new Intent(getActivity(), NotificationDetailActivity.class);
        List<WeekEntry> weeks =  mAdapter.getWeeks();
        in.putExtra(FILE_NAME, weeks.get(itemId-1).getId()+"."+getExtension(weeks.get(itemId-1).getResource_path()));
        startActivity(in);

    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}
