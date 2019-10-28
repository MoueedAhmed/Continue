package com.amoueed.continueapp.db;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LocalNotificationViewModel extends AndroidViewModel {

    private static final String TAG = LocalNotificationViewModel.class.getSimpleName();
    private LiveData<List<LocalNotificationDataEntry>> entries;

    public LocalNotificationViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        entries = database.localNotificationDataDao().loadAllEntries();
    }

    public LiveData<List<LocalNotificationDataEntry>> getEntries() {
        return entries;
    }
}

