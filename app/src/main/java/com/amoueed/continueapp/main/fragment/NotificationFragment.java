package com.amoueed.continueapp.main.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amoueed.continueapp.EnrollmentActivity;
import com.amoueed.continueapp.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    StringBuilder sb;
    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FileInputStream fis = null;
//        try {
//            fis = getActivity().openFileInput("2.txt");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        InputStreamReader isr = new InputStreamReader(fis);
//        BufferedReader bufferedReader = new BufferedReader(isr);
//        sb = new StringBuilder();
//        String line = null;
//        while (true) {
//            try {
//                if (!((line = bufferedReader.readLine()) != null)) break;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            sb.append(line);
//        }



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getContext(), sb, Toast.LENGTH_LONG).show();
    }
}
