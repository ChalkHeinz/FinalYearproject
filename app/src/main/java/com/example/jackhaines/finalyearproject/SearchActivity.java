package com.example.jackhaines.finalyearproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int optionId;

    public static String JSONString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.radioButton).setOnClickListener(this);
        findViewById(R.id.radioButton2).setOnClickListener(this);
        findViewById(R.id.radioButton3).setOnClickListener(this);

        GetService GetService = new GetService(SearchActivity.this);
        GetService.execute("get");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button4:
                try {
                    JSONArray array = new JSONArray(JSONString);

                    for (int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);

                        GetBirdInfoData.lat.add(obj.getString("Lat"));
                        GetBirdInfoData.lon.add(obj.getString("Lon"));
                        GetBirdInfoData.image.add(obj.getString("Image"));
                        GetBirdInfoData.species.add(obj.getString("Species"));
                        GetBirdInfoData.time.add(obj.getString("time"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button5:
                Intent mapIntent = new Intent(SearchActivity.this, MapActivity.class);
                mapIntent.putExtra("option", optionId);
                SearchActivity.this.startActivity(mapIntent);
                break;

            case R.id.radioButton:
                optionId = 0;
                    break;
            case R.id.radioButton2:
                optionId = 1;
                    break;
            case R.id.radioButton3:
                optionId = 2;
                    break;

            // ...
        }
    }


}
