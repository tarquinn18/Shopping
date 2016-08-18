package com.example.skripkin.shopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class purchases extends AppCompatActivity
{
    int selected_list;
    ListView purch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        selected_list = getIntent().getIntExtra("num", 0);
        setTitle(Buy.info.get_lists_list().get(selected_list));

        purch = ((ListView)findViewById(R.id.purchases));
        Prod_list_adapter adapter = new Prod_list_adapter(this, 0, Buy.info.get_list(selected_list));
        purch.setAdapter(adapter);

        purch.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Buy.info.get_list(selected_list).get(i).bought ^= true;
                purch.invalidateViews();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.purchases_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.edit:
                Intent intent = new Intent(purchases.this, edit_list.class);
                intent.putExtra("num", selected_list);
                startActivityForResult(intent, 0);
                break;
            case R.id.save_state: saveState(); break;
            case R.id.make_new_list: setResult(1); finish(); break;
            default: return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Buy.info.save_state();
        purch.invalidateViews();
    }

    public void saveState()
    {
        Buy.info.save_state();
        Toast.makeText(this, "Состояние списка сохранено", Toast.LENGTH_SHORT).show();
    }
}
