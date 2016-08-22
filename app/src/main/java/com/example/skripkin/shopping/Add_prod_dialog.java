package com.example.skripkin.shopping;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.ArrayList;

public class Add_prod_dialog extends DialogFragment
{
    EditText prodname;
    public final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ScrollView dialog_view = (ScrollView) inflater.inflate(R.layout.add_prod_dialog, null);
        Button button = ((Button) dialog_view.findViewById(R.id.voice_button));
        prodname = ((EditText) dialog_view.findViewById(R.id.prodname));
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

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Speech recognition demo");
                startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
            }
        });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == VOICE_RECOGNITION_REQUEST_CODE) && (resultCode != 0))
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) prodname.setText(matches.get(0));
        }
    }
}
