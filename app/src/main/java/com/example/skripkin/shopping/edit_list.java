package com.example.skripkin.shopping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class edit_list extends AppCompatActivity
{
    ListView list;
    ListView products;
    ArrayAdapter<product> buy_adapter;
    ArrayAdapter<product> prod_adapter;
    int selected_list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_list);
        setTitle("Изменение списка");

        list = (ListView) findViewById(R.id.buy_list);
        products = (ListView) findViewById(R.id.prod_list);

        selected_list = getIntent().getIntExtra("num", 0);
        buy_adapter = new Prod_list_adapter(this, android.R.layout.simple_list_item_1, Buy.info.get_list(selected_list));
        prod_adapter = new Prod_list_adapter(this, android.R.layout.simple_list_item_1, Buy.info.get_prod_list());
        list.setAdapter(buy_adapter);
        products.setAdapter(prod_adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                set_amount(i);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                deleteProdFromList(selected_list, i);
                return true;
            }
        });

        products.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Buy.info.add_prod_to_list(selected_list, Buy.info.get_prod_list().get(i));
                list.invalidateViews();
                list.setSelection(list.getCount() - 1);
            }
        });

        products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                deleteProduct(i);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.add_prod)
        {
            DialogFragment dialog = new Add_prod_dialog();
            dialog.show(getSupportFragmentManager(), "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void set_amount(final int j)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final EditText view = new EditText(this);
        view.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog.setTitle("Установите количество товара")
                .setNegativeButton("Отмена", null)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Buy.info.set_amount(selected_list, j, view.getText().toString());
                        list.invalidateViews();
                    }
                }).show();
    }

    public void addProduct(String name, String measure)
    {
        if (name.length() == 0) return;
        String m = "шт";
        switch (measure)
        {
            case "Килограмм": m = "кг"; break;
            case "Грамм": m = "г"; break;
            case "Упаковка": m = "уп"; break;
            case "Литр": m = "л"; break;
            case "Миллилитр": m = "мл"; break;
        }
        Buy.info.add_product(new product(0, name, m), this);
        (((ListView) findViewById(R.id.buy_list))).invalidateViews();
    }

    private void deleteProduct(final int i)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Вы действительно хотите удалить " + Buy.info.get_prod_list().get(i).name + "?")
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j)
                    {
                        Buy.info.remove_prod(i);
                        products.invalidateViews();
                    }
                }).show();
    }

    private void deleteProdFromList(final int cur_list, final int n)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Вы действительно хотите удалить " + Buy.info.get_list(cur_list).get(n).name + "?")
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j)
                    {
                        Buy.info.remove_prod_from_list(cur_list, n);
                        list.requestLayout();
                        list.invalidateViews();
                    }
                }).show();
    }
}
