package com.amoueed.continueapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocalNotificationDataDao {

    @Query("SELECT * FROM notification_entry ORDER BY updatedAt")
    LiveData<List<LocalNotificationDataEntry>> loadAllEntries();

    @Insert
    void insertEntry(LocalNotificationDataEntry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(LocalNotificationDataEntry entry);

    @Delete
    void deleteEntry(LocalNotificationDataEntry entry);

    @Query("SELECT * FROM notification_entry WHERE file_name = :file_name")
    LocalNotificationDataEntry loadEntryByFileName(String file_name);
}

