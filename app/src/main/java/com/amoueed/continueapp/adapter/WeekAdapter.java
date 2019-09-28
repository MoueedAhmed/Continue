package com.amoueed.continueapp.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amoueed.continueapp.R;
import com.amoueed.continueapp.database.WeekEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
    private List<WeekEntry> mWeekEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public WeekAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.week_layout, parent, false);

        return new WeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {

        WeekEntry weekEntry = mWeekEntries.get(position);
        String resource_path = weekEntry.getResource_path();
        int week_num = weekEntry.getWeek_num();
        String week_date = dateFormat.format(weekEntry.getWeek_date());

        holder.week_resource_tv.setText("Week "+weekEntry.getWeek_num());
        holder.week_date_tv.setText(week_date);

        String week_num_string = "" + week_num;
        holder.week_no_tv.setText(week_num_string);

        GradientDrawable weekCircle = (GradientDrawable) holder.week_no_tv.getBackground();
        int weekColor = getWeekColor();
        weekCircle.setColor(weekColor);
    }


    private int getWeekColor() {

        return ContextCompat.getColor(mContext, R.color.materialYellow);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mWeekEntries == null) {
            return 0;
        }
        return mWeekEntries.size();
    }

    public void setWeeks(List<WeekEntry> weekEntries) {
        mWeekEntries = weekEntries;
        notifyDataSetChanged();
    }

    public List<WeekEntry> getWeeks() {
        return mWeekEntries;
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class WeekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView week_resource_tv;
        TextView week_date_tv;
        TextView week_no_tv;

        public WeekViewHolder(View itemView) {
            super(itemView);

            week_resource_tv = itemView.findViewById(R.id.week_resource_tv);
            week_date_tv = itemView.findViewById(R.id.week_date_tv);
            week_no_tv = itemView.findViewById(R.id.week_no_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mWeekEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}