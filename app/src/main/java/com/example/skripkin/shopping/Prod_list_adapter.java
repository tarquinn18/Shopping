package com.example.skripkin.shopping;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Prod_list_adapter extends ArrayAdapter<product>
{
    List<product> products;
    Context context;

    public Prod_list_adapter(Context c, int resource, List<product> objects)
    {
        super(c, resource, objects);
        products = objects;
        context = c;
    }

    public View getView(int position, View v, ViewGroup parent)
    {
        View view = v;
        if (view == null) view = ((LayoutInflater) this.getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.prod_item, null);
        product this_prod = products.get(position);
        ((TextView)view.findViewById(R.id.prod_left)).setText(String.valueOf(position + 1) + ". " + this_prod.name);
        ((TextView)view.findViewById(R.id.prod_right)).setText((this_prod.amount>0?String.valueOf(this_prod.amount):"") + " " + this_prod.measure);
        if (this_prod.bought) view.setBackgroundColor(context.getResources().getColor(R.color.green_background));
        else view.setBackgroundColor(Color.WHITE);
        return view;
    }
}
