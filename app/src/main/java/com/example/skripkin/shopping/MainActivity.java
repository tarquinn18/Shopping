package com.example.skripkin.shopping;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    ArrayAdapter<String> adapter_lists;
    ListView lists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Buy.info.init_buy_info(getExternalFilesDir(null).getPath());
        setTitle("Cписки");

        lists = (ListView) findViewById(R.id.lists);
        adapter_lists = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Buy.info.get_lists_list());
        lists.setAdapter(adapter_lists);
        lists.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View itemClicked, int i, long l)
            {
                Intent intent = new Intent(MainActivity.this, purchases.class);
                intent.putExtra("num", i);
                startActivityForResult(intent, i);
            }
        });
        lists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View itemClicked, int i, long l)
            {
                remove_list(i, ((TextView)itemClicked).getText().toString());
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.add_list)
        {
            Add_list_dialog.newInstance(-1).show(getSupportFragmentManager(), "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == 1)
        {
            DialogFragment dialog = Add_list_dialog.newInstance(requestCode);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(dialog, null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void add_list(String name, String date, int n)
    {
        String newList;
        if (name.length() > 0) newList = name;
        else newList = "Новый список";
        newList += " (" + date + ")";
        Buy.info.add_list(newList, n);
        lists.invalidateViews();
    }

    private void remove_list(final int n, String name)
    {
        final AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
        dialog1.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int j)
            {
                Buy.info.remove_list(n);
                lists.invalidateViews();
            }
        }).setTitle("Вы хотите удалить \"" + name + "\"?")
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {}

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
