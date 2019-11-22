package com.amoueed.continueapp.main.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        View rootView = inflater.inflate(R.layout.fragment_resource, container, false);

        Button epi_button = rootView.findViewById(R.id.epi_button);
        epi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.epi.gov.pk/"));
                startActivity(intent);
            }
        });

        Button aku_button = rootView.findViewById(R.id.aku_button);
        aku_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hospitals.aku.edu/pakistan/AboutUs/News/Pages/community-health-centre.aspx"));
                startActivity(intent);
            }
        });

        Button epis_button = rootView.findViewById(R.id.epis_button);
        epis_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.epi.gov.pk/immunisation-schedule/"));
                startActivity(intent);
            }
        });

        Button who_button = rootView.findViewById(R.id.who_button);
        who_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.who.int/ith/vaccines/en/"));
                startActivity(intent);
            }
        });
        return rootView;
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
