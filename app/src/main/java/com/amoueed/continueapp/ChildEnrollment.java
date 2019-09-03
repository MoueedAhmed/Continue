package com.amoueed.continueapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChildEnrollment {
    private String childName;
    private String childDOB;
    private String childGender;
    private String childMR;
    private String contactNo;
    private String childRelative;
    private String mode;
    private String language;
    private String barrier;
    private String preferredTime;

    public ChildEnrollment() {
    }

    public ChildEnrollment(String childName, String childDOB, String childGender,
                           String childMR, String contactNo, String childRelative,
                           String mode, String language, String barrier, String preferredTime) {
        this.childName = childName;
        this.childDOB = childDOB;
        this.childGender = childGender;
        this.childMR = childMR;
        this.contactNo = contactNo;
        this.childRelative = childRelative;
        this.mode = mode;
        this.language = language;
        this.barrier = barrier;
        this.preferredTime = preferredTime;
    }
}
