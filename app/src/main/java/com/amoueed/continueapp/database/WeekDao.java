package com.amoueed.continueapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeekDao {

    @Query("SELECT * FROM week ORDER BY week_num")
    LiveData<List<WeekEntry>> loadAllWeeks();

    @Insert
    void insertWeek(WeekEntry weekEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeek(WeekEntry weekEntry);

    @Delete
    void deleteWeek(WeekEntry weekEntry);
}