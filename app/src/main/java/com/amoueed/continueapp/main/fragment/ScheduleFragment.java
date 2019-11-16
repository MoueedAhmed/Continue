package com.amoueed.continueapp.main.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amoueed.continueapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        SharedPreferences sharedPref =getContext().getSharedPreferences("content_identifier", Context.MODE_PRIVATE);
        String content_identifier = sharedPref.getString("dob","");

        String dobString = content_identifier;
        Date dob = null;
        try {
            dob=new SimpleDateFormat("dd/MM/yy").parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cbirth = Calendar.getInstance();
        cbirth.setTime(dob);

        Calendar c6 = Calendar.getInstance();
        c6.setTime(dob);
        c6.add(Calendar.DATE, 42);

        Calendar c10 = Calendar.getInstance();
        c10.setTime(dob);
        c10.add(Calendar.DATE, 70);

        Calendar c14 = Calendar.getInstance();
        c14.setTime(dob);
        c14.add(Calendar.DATE, 98);

        Calendar c20 = Calendar.getInstance();
        c20.setTime(dob);
        c20.add(Calendar.DATE, 140);



        schedule_mcal =rootView.findViewById(R.id.schedule_mcal);
        schedule_mcal.markDate(cbirth.get(Calendar.YEAR), cbirth.get(Calendar.MONTH)+1, cbirth.get(Calendar.DATE));
        schedule_mcal.markDate(c6.get(Calendar.YEAR), c6.get(Calendar.MONTH)+1, c6.get(Calendar.DATE));
        schedule_mcal.markDate(c10.get(Calendar.YEAR), c10.get(Calendar.MONTH)+1, c10.get(Calendar.DATE));
        schedule_mcal.markDate(c14.get(Calendar.YEAR), c14.get(Calendar.MONTH)+1, c14.get(Calendar.DATE));
        schedule_mcal.markDate(c20.get(Calendar.YEAR), c20.get(Calendar.MONTH)+1, c20.get(Calendar.DATE));

        TextView circle_6_tv = rootView.findViewById(R.id.circle_6_tv);
        TextView vaccine_6_tv = rootView.findViewById(R.id.vaccine_6_tv);
        TextView date_6_tv = rootView.findViewById(R.id.date_6_tv);

        TextView circle_10_tv = rootView.findViewById(R.id.circle_10_tv);
        TextView vaccine_10_tv = rootView.findViewById(R.id.vaccine_10_tv);
        TextView date_10_tv = rootView.findViewById(R.id.date_10_tv);

        TextView circle_14_tv = rootView.findViewById(R.id.circle_14_tv);
        TextView vaccine_14_tv = rootView.findViewById(R.id.vaccine_14_tv);
        TextView date_14_tv = rootView.findViewById(R.id.date_14_tv);

        TextView circle_20_tv = rootView.findViewById(R.id.circle_20_tv);
        TextView vaccine_20_tv = rootView.findViewById(R.id.vaccine_20_tv);
        TextView date_20_tv = rootView.findViewById(R.id.date_20_tv);

        circle_6_tv.setText(c6.get(Calendar.DATE)+"");
        vaccine_6_tv.setText("6th week vaccine");
        Date d6 = c6.getTime();
        DateFormat df6 = new SimpleDateFormat("dd-MMM-yyyy");
        date_6_tv.setText(df6.format(d6));

        circle_10_tv.setText(c10.get(Calendar.DATE)+"");
        vaccine_10_tv.setText("10th week vaccine");
        Date d10 = c10.getTime();
        DateFormat df10 = new SimpleDateFormat("dd-MMM-yyyy");
        date_10_tv.setText(df10.format(d10));

        circle_14_tv.setText(c14.get(Calendar.DATE)+"");
        vaccine_14_tv.setText("14th week vaccine");
        Date d14 = c14.getTime();
        DateFormat df14 = new SimpleDateFormat("dd-MMM-yyyy");
        date_14_tv.setText(df14.format(d14));

        circle_20_tv.setText(c20.get(Calendar.DATE)+"");
        vaccine_20_tv.setText("20th week vaccine");
        Date d20 = c20.getTime();
        DateFormat df20 = new SimpleDateFormat("dd-MMM-yyyy");
        date_20_tv.setText(df20.format(d20));

        return rootView;
    }

}
