package com.example.skripkin.shopping;


public class product
{
    int amount;
    String name;
    String measure;
    boolean bought;

    product(int a, String n, String m)
    {
        amount = a;
        name = n;
        measure = m;
        bought = false;
    }
}