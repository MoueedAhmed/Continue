package com.amoueed.continueapp.firebasemodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ScheduleFragmentModel {

    public String enter;
    public String exit;

    public ScheduleFragmentModel() {
    }

    public ScheduleFragmentModel(String enter, String exit) {
        this.enter = enter;
        this.exit = exit;
    }
}
