package com.golynskyy.ipm.motivationapp.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.os.Build;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.golynskyy.ipm.motivationapp.NoteAddActivity;
import com.golynskyy.ipm.motivationapp.R;

import java.util.Calendar;

/**
 * Created by Dep5 on 07.08.2017.
 */

public class DialogDateAndTime extends DialogFragment {

    public interface DateTimeSettedDialogListener {
        void onFinishDateTimeDialogDialog(long timeStamp, String tag);
    }

    private DateTimeSettedDialogListener listener;

    private Button btnSet;
    private DatePicker datePickerSet;
    private TimePicker timePickerSet;

    public DialogDateAndTime() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Set date and time");
        //dialog cannot be closed by back button
        setCancelable(false);
        View v = inflater.inflate(R.layout.fragment_datetime_dialog, null);
        btnSet = (Button) v.findViewById(R.id.buttonSetDateTimeDialog);
        datePickerSet = (DatePicker)v.findViewById(R.id.datePickerSet);
        // set calendar only spinner mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePickerSet.setCalendarViewShown(false);
        }
        timePickerSet = (TimePicker)v.findViewById(R.id.timePickerSet);
        //set time 24 hour design
            timePickerSet.setIs24HourView(true);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                //date
                calendar.set(Calendar.YEAR,datePickerSet.getYear());
                calendar.set(Calendar.MONTH,datePickerSet.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH,datePickerSet.getDayOfMonth());
                //time
                calendar.set(Calendar.HOUR,timePickerSet.getCurrentHour());
                calendar.set(Calendar.MINUTE,timePickerSet.getCurrentMinute());
                //send data to NoteAddActivity :)
                listener = (DateTimeSettedDialogListener) getActivity();
                listener.onFinishDateTimeDialogDialog(calendar.getTimeInMillis(),getTag());
                dismiss();
            }
        });
        return v;
    }




  /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Dialog","Instantiate callback");
        // Verify that the NoteAdd Activity implements the callback interface
        try {
            // Instantiate the DateTimeSettedDialogListener
            listener = (DateTimeSettedDialogListener) context;
        } catch (ClassCastException e) {
            // if NoteAdd Activity doesn't implement the interface, throw usual exception
            throw new ClassCastException(context.toString()
                    + " must implement DateTimeSettedDialogListener");
        }
        Log.d("Dialog","Instantiate was resulting");
    }
*/
}
