package com.example.jackhaines.finalyearproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    static public ListView listView;

    static public String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = (ListView) findViewById(R.id.listView);
        findViewById(R.id.button4).setOnClickListener(this);


        BackendService backendService = new BackendService(SearchActivity.this);
        backendService.execute("get");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button4:
                String bam = test;
                break;
            // ...
        }
    }


}
