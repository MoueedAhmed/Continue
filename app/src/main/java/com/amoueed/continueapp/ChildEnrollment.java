package com.amoueed.continueapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChildEnrollment {
    public String childName;
    public String childDOB;
    public String childGender;
    public String childMR;
    public String contactNo;
    public String childRelative;
    public String mode;
    public String language;
    public String barrier;
    public String preferredTime;

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
