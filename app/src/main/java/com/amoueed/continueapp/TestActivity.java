package com.amoueed.continueapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.os.Bundle;
import android.view.View;


import com.amoueed.continueapp.database.AppDatabase;
import com.amoueed.continueapp.database.WeekEntry;

import java.util.Date;

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

        mDb = AppDatabase.getInstance(getApplicationContext());

    }

    @Override
    public void onItemClickListener(int itemId) {

    }

    public void b1Clicked(View view) {

        Date date = new Date();
        WeekEntry weekEntry = new WeekEntry("resource/path", 1, date);
        mDb.weekDao().insertWeek(weekEntry);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setWeeks(mDb.weekDao().loadAllWeeks());
    }
}

