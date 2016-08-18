package com.example.skripkin.shopping;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import java.util.Calendar;

public class Add_list_dialog extends DialogFragment
{
    int day = 0;
    int month = 0;
    int year = 0;
    int n = 0;

    static Add_list_dialog newInstance(int n)
    {
        Add_list_dialog f = new Add_list_dialog();
        Bundle args = new Bundle();
        args.putInt("num", n);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ScrollView dialog_view = (ScrollView) inflater.inflate(R.layout.add_list_dialog, null);
        n = getArguments().getInt("num");
        Calendar c = Calendar.getInstance();
        if (c != null)
        {
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);
        }
        DatePicker datePicker = ((DatePicker) dialog_view.findViewById(R.id.calendar));
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener()
        {
            @Override
            public void onDateChanged(DatePicker datePicker, int s_year, int s_month, int s_day)
            {
                year = s_year;
                month = s_month;
                day = s_day;
            }
        });

        builder.setTitle("Выберите имя списка и предполагаемую дату")
                .setView(dialog_view)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        month++;
                        ((MainActivity)getActivity()).add_list(
                                ((EditText) dialog_view.findViewById(R.id.listname)).getText().toString(),
                                (day < 10 ? "0" : "") + ((Integer)day).toString() + (month < 10 ? ".0" : ".") + ((Integer)month).toString() + "." + ((Integer) year).toString()
                                , n);
                    }
                })
                .setNegativeButton("Отмена", null);
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {}
}
