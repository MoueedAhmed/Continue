package com.amoueed.continueapp.main.fragment;


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

import com.amoueed.continueapp.ContentIdentifier;
import com.amoueed.continueapp.R;
import com.amoueed.continueapp.adapter.LocalNotificationDataAdapter;
import com.amoueed.continueapp.db.AppDatabase;
import com.amoueed.continueapp.db.AppExecutors;
import com.amoueed.continueapp.db.LocalNotificationDataEntry;
import com.amoueed.continueapp.db.LocalNotificationViewModel;
import com.amoueed.continueapp.ui.NotificationDetailActivity;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class NotificationFragment extends Fragment implements LocalNotificationDataAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private LocalNotificationDataAdapter mAdapter;
    private AppDatabase mDb;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        SharedPreferences sharedPref =getContext().getSharedPreferences("content_identifier", Context.MODE_PRIVATE);
        String content_identifier = sharedPref.getString("content_identifier","");
        if(content_identifier.startsWith("t")){
            insertLocalNotificationData("txt");
        }
        else{
            insertLocalNotificationData("wav");
        }


        mRecyclerView = rootView.findViewById(R.id.week_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LocalNotificationDataAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity().getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//            // Called when a user swipes left or right on a ViewHolder
//            @Override
//            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        int position = viewHolder.getAdapterPosition();
//                        List<LocalNotificationDataEntry> tasks = mAdapter.getEntries();
//                        //Call deleteTask in the taskDao with the task at that position
//                        mDb.localNotificationDataDao().deleteEntry(tasks.get(position));
//                        //Call retrieveTasks method to refresh the UI
//                    }
//                });
//            }
//        }).attachToRecyclerView(mRecyclerView);

        setupLocalNotificationViewModel();
        return rootView;
    }

    private void insertLocalNotificationData(String extension) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("count", Context.MODE_PRIVATE);
        int count = sharedPref.getInt("count",0);
        if(count<=1){
            if(extension.equals("txt")){
                String file_name = count+"."+extension;
                File file = new File(getContext().getFilesDir(), file_name);
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                    addLocalNotificationDataEntryToDB("CoNTINuE",text.substring(0,50)+"......",file_name);
                } catch (IOException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            else{
                String file_name = count+"."+extension;
                addLocalNotificationDataEntryToDB("CoNTINuE","You have audio message",file_name);
            }
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("count",count+1);
        editor.commit();
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent in = new Intent(getActivity(), NotificationDetailActivity.class);
        List<LocalNotificationDataEntry> entries = mAdapter.getEntries();
        LocalNotificationDataEntry entry = entries.get(itemId);
        in.putExtra("file_name",entry.getFile_name());
        startActivity(in);
//        List<WeekEntry> weeks =  mAdapter.getWeeks();
//        in.putExtra(FILE_NAME, weeks.get(itemId-1).getId()+"."+getExtension(weeks.get(itemId-1).getResource_path()));
//        startActivity(in);

    }

    private void setupLocalNotificationViewModel() {
        //read from db and display on RecyclerView
        LocalNotificationViewModel viewModel = ViewModelProviders.of(this).get(LocalNotificationViewModel.class);

        viewModel.getEntries().observe(this, new Observer<List<LocalNotificationDataEntry>>() {
            @Override
            public void onChanged(List<LocalNotificationDataEntry> localNotificationDataEntries) {
                mAdapter.setEntries(localNotificationDataEntries);
            }
        });


    }

    //create one row of LocalNotificationDataEntry in db
    private void addLocalNotificationDataEntryToDB(String notification_type, String content, String file_name){
        Date date = new Date();
        final LocalNotificationDataEntry entry = new LocalNotificationDataEntry
                (notification_type, content,file_name, date,0);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.localNotificationDataDao().insertEntry(entry);
            }
        });
    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}
