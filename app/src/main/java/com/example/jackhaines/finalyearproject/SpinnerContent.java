package com.example.jackhaines.finalyearproject;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class SpinnerContent {

    public ArrayAdapter<String> PigeonsAndDoves(){
        ArrayAdapter<String> list = null;
        list.add("Rock Dove");
        list.add("Woodpigeon");
        list.add("Stock Dove");
        list.add("Turtle Dove");
        list.add("Collared Dove");

        return list;
    }
}
