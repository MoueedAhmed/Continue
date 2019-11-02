package com.amoueed.continueapp.firebasemodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class NotificationDetailActivityModel {
    public String enter;
    public String exit;
    public String read_main;
    public String read_0;
    public String read_1;
    public String notification;

    public NotificationDetailActivityModel() {
    }

    public NotificationDetailActivityModel(String enter, String exit, String read_main, String read_0, String read_1, String notification) {
        this.enter = enter;
        this.exit = exit;
        this.read_main = read_main;
        this.read_0 = read_0;
        this.read_1 = read_1;
        this.notification = notification;
    }
}
