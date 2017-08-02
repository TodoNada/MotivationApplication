package com.golynskyy.ipm.motivationapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.golynskyy.ipm.motivationapp.R;
import com.golynskyy.ipm.motivationapp.basic.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dep5 on 26.07.2017.
 */


public class RemindersAdapter<T extends BasicReminder> extends ArrayAdapter<T> {

    protected List reminderList;       // The list of reminders that will be displayed
    protected LayoutInflater inflater;
    protected Context context;


    public RemindersAdapter(Context context, List<T> reminderList) {
        super(context, R.layout.fragment_short_reminders, reminderList);

        this.context = context;
        this.reminderList = reminderList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    //-- view holder
    public class ViewHolder {

        protected LinearLayout innerLayout;
        protected TextView tvReminderTitle;
        protected TextView tvReminderDescription;
        protected TextView tvReminderTargetDate;
        protected ImageView ivReminderType;
        protected ImageView ivReminderVibration;
        protected ImageView ivReminderSound;
        protected ImageView ivReminderLight;
        //TODO: realize ImageViews
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        int id = R.layout.fragment_short_reminders;

        if (convertView == null) {
            view = inflater.inflate( id, parent, false);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.innerLayout = (LinearLayout) view.findViewById(R.id.mainLinearLayoutFragmentShortNotes);
            viewHolder.tvReminderTitle = (TextView) view.findViewById(R.id.textViewNameShortReminder);
            viewHolder.tvReminderDescription = (TextView) view.findViewById(R.id.textViewDetailsShortReminder);
            viewHolder.tvReminderTargetDate = (TextView) view.findViewById(R.id.texViewTargetDateValueShortReminder);
            view.setTag(R.id.reminder_item_object,viewHolder);
        }
        else
            view = convertView;



        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.reminder_item_object);

        BasicReminder reminderItem = (BasicReminder)reminderList.get(position);

        view.setTag(R.id.reminder_item_id, reminderItem.getId());
        view.setTag(R.id.reminder_item_index, position);

        if(viewHolder.innerLayout != null)
        {
            // viewHolder.innerLayout.setOnClickListener(this);
            viewHolder.innerLayout.setTag(R.id.reminder_item_index, position);
        }

        viewHolder.tvReminderTitle.setText(reminderItem.getTitle());
        viewHolder.tvReminderDescription.setText(reminderItem.getDetails());
        viewHolder.tvReminderTargetDate.setText(""+reminderItem.getTargetDate());
        //TODO: convert TargetDate into DateTime view

        /*
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        Date lastModifiedDate = new Date((long)noteItem.getLastModified()*1000);

        // Displays the last modified date of the note
        if(Utils.isDateToday(noteItem.getLastModified()))
        {
            viewHolder.date.setText(getContext().
                    getString(R.string.today_capital) + "\n" + hourFormat.format(lastModifiedDate)
            );
        }
        else if(Utils.isDateToday(noteItem.getLastModified()))
        {
            viewHolder.date.setText(getContext().
                    getString(R.string.yesterday_capital) +"\n"+hourFormat.format(lastModifiedDate)
            );
        }
        else
        {
            Calendar lastModifiedCalendar = Calendar.getInstance();
            lastModifiedCalendar.setTime(lastModifiedDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
            viewHolder.date.setText(
                    dateFormat.format(new Date(((long)noteItem.getLastModified()*1000))));
        }
        */
        return view;
    }



}
