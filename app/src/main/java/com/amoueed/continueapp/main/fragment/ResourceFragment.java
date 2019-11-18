package com.amoueed.continueapp.main.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoueed.continueapp.R;
import com.amoueed.continueapp.firebasemodel.ResourceFragmentModel;
import com.amoueed.continueapp.firebasemodel.ScheduleFragmentModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResourceFragment extends Fragment {

    private String enter;
    private String exit;
    private DatabaseReference mDatabase;

    public ResourceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resource, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        syncWithFirebaseDatabase();
        enter = System.currentTimeMillis() +"";
    }

    @Override
    public void onPause() {
        super.onPause();
        exit = System.currentTimeMillis() +"";

        insertResourceFragmentModelToFirebase();

    }

    private void syncWithFirebaseDatabase() {
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (Exception e){
            Log.e("ResourceFragment", e.getMessage());
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    private void insertResourceFragmentModelToFirebase() {
        ResourceFragmentModel model = new ResourceFragmentModel(enter,exit);
        SharedPreferences sharedPref = getContext().getSharedPreferences("content_identifier", Context.MODE_PRIVATE);
        String childMR = sharedPref.getString("mr_number","");
        mDatabase.child("app_data").child(childMR).child("ResourceFragment").push().setValue(model);
    }

}
