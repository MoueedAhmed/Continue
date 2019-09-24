package com.amoueed.continueapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.os.Bundle;


import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class TestActivity extends AppCompatActivity implements WeekAdapter.ItemClickListener {
    private RecyclerView mRecyclerView;
    private WeekAdapter mAdapter;
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

    }

    @Override
    public void onItemClickListener(int itemId) {

    }
}

