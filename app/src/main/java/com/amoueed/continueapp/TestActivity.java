package com.amoueed.continueapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.amoueed.continueapp.database.AppDatabase;
import com.amoueed.continueapp.database.AppExecutors;
import com.amoueed.continueapp.database.WeekEntry;

import java.util.Date;
import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class TestActivity extends AppCompatActivity implements WeekAdapter.ItemClickListener {
    private RecyclerView mRecyclerView;
    private WeekAdapter mAdapter;

    private AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mRecyclerView = findViewById(R.id.week_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WeekAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
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

        mDb = AppDatabase.getInstance(getApplicationContext());

        setupViewModel();

    }

    @Override
    public void onItemClickListener(int itemId) {

    }

    public void b1Clicked(View view) {

        Date date = new Date();
        final WeekEntry weekEntry = new WeekEntry("resource/path", 1, date);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.weekDao().insertWeek(weekEntry);
            }
        });


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
}

