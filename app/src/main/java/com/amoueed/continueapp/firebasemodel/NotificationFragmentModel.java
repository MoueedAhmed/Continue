package com.amoueed.continueapp.firebasemodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class NotificationFragmentModel {

    public String enter;
    public String exit;

    public NotificationFragmentModel() {
    }

    public NotificationFragmentModel(String enter, String exit) {
        this.enter = enter;
        this.exit = exit;
    }
}
