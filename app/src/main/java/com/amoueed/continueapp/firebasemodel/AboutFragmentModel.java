package com.amoueed.continueapp.firebasemodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AboutFragmentModel {
    public String enter;
    public String exit;

    public AboutFragmentModel() {
    }

    public AboutFragmentModel(String enter, String exit) {
        this.enter = enter;
        this.exit = exit;
    }
}
