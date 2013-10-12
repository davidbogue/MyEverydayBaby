package com.myeverydaybaby.fragments;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by davidbogue on 10/12/13.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        try{
            DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener)getActivity();
            listener.onDateSet(view, year, month, day );
        }catch (ClassCastException cce){
            throw new ClassCastException(getActivity().toString()+" Must implement OnDateSetListener");
        }
    }
}
