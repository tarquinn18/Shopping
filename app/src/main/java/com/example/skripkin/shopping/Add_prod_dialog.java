package com.example.skripkin.shopping;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

public class Add_prod_dialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ScrollView dialog_view = (ScrollView) inflater.inflate(R.layout.add_prod_dialog, null);
        //EditText editText = ((EditText) dialog_view.findViewById(R.id.prodname));
        final Spinner spinner = ((Spinner) dialog_view.findViewById(R.id.measure));
        spinner.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.measures, android.R.layout.simple_spinner_dropdown_item));


        builder.setTitle("Выберите название продукта и единицу измерения")
                .setView(dialog_view)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        ((edit_list)getActivity()).addProduct(
                                ((EditText) dialog_view.findViewById(R.id.prodname)).getText().toString(),
                                spinner.getSelectedItem().toString());
                    }
                }).setNegativeButton("Отмена", null);
        return builder.create();
    }
}
