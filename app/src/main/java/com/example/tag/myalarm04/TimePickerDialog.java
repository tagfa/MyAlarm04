package com.example.tag.myalarm04;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by tag on 2017/05/27.
 */

public class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        android.app.TimePickerDialog timePickerDialog =
                new android.app.TimePickerDialog(getActivity(),(NewAlarmActivity)getActivity(),hour,minute,true);

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
