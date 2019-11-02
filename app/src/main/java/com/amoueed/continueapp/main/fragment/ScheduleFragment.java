package com.amoueed.continueapp.main.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoueed.continueapp.R;

import sun.bob.mcalendarview.MCalendarView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {
    private MCalendarView schedule_mcal;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        schedule_mcal =rootView.findViewById(R.id.schedule_mcal);
        schedule_mcal.markDate(2019,11,7);
        schedule_mcal.markDate(2019,11,15);
        schedule_mcal.markDate(2019,12,16);

        return rootView;
    }

}
