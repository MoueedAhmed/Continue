package com.amoueed.continueapp.firebasemodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ResourceFragmentModel {

    public String enter;
    public String exit;

    public ResourceFragmentModel() {
    }

    public ResourceFragmentModel(String enter, String exit) {
        this.enter = enter;
        this.exit = exit;
    }
}
