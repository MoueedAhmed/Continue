package com.amoueed.continueapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amoueed.continueapp.R;
import com.amoueed.continueapp.db.LocalNotificationDataEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.getColor;

public class LocalNotificationDataAdapter extends RecyclerView.Adapter<LocalNotificationDataAdapter.LocalNotificationDataViewHolder>{
    private static final String DATE_FORMAT = "dd/MM/yyy";
    final private ItemClickListener mItemClickListener;
    private Context mContext;
    private List<LocalNotificationDataEntry> mEntries;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public LocalNotificationDataAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public LocalNotificationDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.local_notification_data_item, parent, false);

        return new LocalNotificationDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalNotificationDataViewHolder holder, int position) {
        LocalNotificationDataEntry entry = mEntries.get(position);
        String notification_type = entry.getNotification_type();
        String content = entry.getContent();
        String updatedAt = dateFormat.format(entry.getUpdatedAt());
        int read_flag = entry.getFlag();


        //Set values
        holder.notification_type_tv.setText(notification_type);
        holder.date_tv.setText(updatedAt);
        holder.content_tv.setText(content);

        if(read_flag == 0){
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            holder.content_tv.setTypeface(boldTypeface);
            holder.container_ll.setBackgroundColor(getColor(this.mContext, R.color.lightBlue));
        }else{
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
            holder.content_tv.setTypeface(boldTypeface);
            holder.container_ll.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        if (mEntries == null) {
            return 0;
        }
        return mEntries.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    /**
     * When data changes, this method updates the list
     * and notifies the adapter to use the new values on it
     */
    public void setEntries(List<LocalNotificationDataEntry> entries) {
        mEntries = entries;
        notifyDataSetChanged();
    }

    public List<LocalNotificationDataEntry> getEntries() {
        return mEntries;
    }


    class LocalNotificationDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout container_ll;
        TextView notification_type_tv;
        TextView content_tv;
        TextView date_tv;
        public LocalNotificationDataViewHolder(View itemView) {
            super(itemView);
            container_ll = itemView.findViewById(R.id.container_ll);
            notification_type_tv = itemView.findViewById(R.id.notification_type_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
//            int elementId = mEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(getAdapterPosition());
        }
    }
}
