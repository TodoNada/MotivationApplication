package com.golynskyy.ipm.motivationapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.golynskyy.ipm.motivationapp.R;
import com.golynskyy.ipm.motivationapp.basic.*;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dep5 on 26.07.2017.
 */

public class NotesAdapter <T extends BasicNote> extends ArrayAdapter<T> {


    protected List noteList;       // The list of notes that will be displayed
    protected LayoutInflater inflater;
    protected Context context;



    public NotesAdapter(Context context, List<T> noteList) {
        super(context, R.layout.fragment_short_notes, noteList);

        this.context = context;
        this.noteList = noteList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    //-- view holder
    public class ViewHolder {

        protected LinearLayout innerLayout;
        protected TextView noteTitle;
        protected TextView noteDescription;
        protected ProgressBar pbTimeProgress;
        protected ProgressBar pbTaskProgress;
        protected ImageView ivReminderArePresent;
        protected ImageView ivNoteImportancy;
        //TODO: realize ImageViews
     }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        int id = R.layout.fragment_short_notes;

        if (convertView == null) {
            view = inflater.inflate( id, parent, false);
            final ViewHolder viewHolder = new ViewHolder();

                viewHolder.innerLayout = (LinearLayout) view.findViewById(R.id.mainLinearLayoutFragmentShortNotes);
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.textViewShortName);
                viewHolder.noteDescription = (TextView) view.findViewById(R.id.textViewShortDescription);
                viewHolder.pbTimeProgress = (ProgressBar) view.findViewById(R.id.progressBarShortTimeProgress);
                viewHolder.pbTaskProgress = (ProgressBar) view.findViewById(R.id.progressBarShortTaskPercentage);
                view.setTag(R.id.note_item_object,viewHolder);
        }
        else
            view = convertView;



        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.note_item_object);

        BasicNote noteItem = (BasicNote) noteList.get(position);

        view.setTag(R.id.note_item_id, noteItem.getId());
        view.setTag(R.id.note_item_index, position);

        if(viewHolder.innerLayout != null)
        {
           // viewHolder.innerLayout.setOnClickListener(this);
            viewHolder.innerLayout.setTag(R.id.note_item_index, position);
        }

        viewHolder.noteTitle.setText(noteItem.getName());
        viewHolder.noteDescription.setText(noteItem.getDescription());
        viewHolder.pbTaskProgress.setProgress(noteItem.getStatus());
        viewHolder.pbTimeProgress.setProgress((int)(100*(noteItem.getLastModified()-noteItem.getBeginDate())/(noteItem.getEndDate()-noteItem.getBeginDate())));

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
