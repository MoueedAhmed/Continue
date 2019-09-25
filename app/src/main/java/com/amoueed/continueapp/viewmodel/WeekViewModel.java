package com.amoueed.continueapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amoueed.continueapp.database.AppDatabase;
import com.amoueed.continueapp.database.WeekEntry;

import java.util.List;

public class WeekViewModel extends AndroidViewModel {

    private static final String TAG = WeekViewModel.class.getSimpleName();

    private LiveData<List<WeekEntry>> weeks;

    public WeekViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        weeks = database.weekDao().loadAllWeeks();
    }

    public LiveData<List<WeekEntry>> getWeeks() {
        return weeks;
    }
}