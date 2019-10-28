package com.amoueed.continueapp.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notification_entry")
public class LocalNotificationDataEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String notification_type;
    private String content;
    private String file_name;
    private Date updatedAt;
    private int flag;

    @Ignore
    public LocalNotificationDataEntry(String notification_type, String content, String file_name, Date updatedAt, int flag) {
        this.notification_type = notification_type;
        this.content = content;
        this.file_name = file_name;
        this.updatedAt = updatedAt;
        this.flag = flag;
    }

    public LocalNotificationDataEntry(int id, String notification_type, String content, String file_name, Date updatedAt, int flag) {
        this.id = id;
        this.notification_type = notification_type;
        this.content = content;
        this.file_name = file_name;
        this.updatedAt = updatedAt;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}