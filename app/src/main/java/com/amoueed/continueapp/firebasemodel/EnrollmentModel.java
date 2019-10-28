package com.amoueed.continueapp.firebasemodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EnrollmentModel {

    public String childMR;
    public String childDOB;
    public String mode;
    public String language;
    public String barrier;
    public String preferredTime;

    public EnrollmentModel() {
    }

    public EnrollmentModel(String childMR, String childDOB, String mode, String language, String barrier, String preferredTime) {
        this.childMR = childMR;
        this.childDOB = childDOB;
        this.mode = mode;
        this.language = language;
        this.barrier = barrier;
        this.preferredTime = preferredTime;
    }
}
