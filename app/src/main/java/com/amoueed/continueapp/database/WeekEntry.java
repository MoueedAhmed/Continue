package com.amoueed.continueapp.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "week")
public class WeekEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String resource_path;
    private int week_num;
    private Date week_date;

    @Ignore
    public WeekEntry(String resource_path, int week_num, Date week_date) {
        this.resource_path = resource_path;
        this.week_num = week_num;
        this.week_date = week_date;
    }

    public WeekEntry(int id, String resource_path, int week_num, Date week_date) {
        this.id = id;
        this.resource_path = resource_path;
        this.week_num = week_num;
        this.week_date = week_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResource_path() {
        return resource_path;
    }

    public void setResource_path(String resource_path) {
        this.resource_path = resource_path;
    }

    public int getWeek_num() {
        return week_num;
    }

    public void setWeek_num(int week_num) {
        this.week_num = week_num;
    }

    public Date getWeek_date() {
        return week_date;
    }

    public void setWeek_date(Date week_date) {
        this.week_date = week_date;
    }
}