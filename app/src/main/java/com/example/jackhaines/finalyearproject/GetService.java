package com.example.jackhaines.finalyearproject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GetService extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;

    GetService(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            //creating a URL
            URL url = new URL("http://35.246.45.206/get.php");

            //Opening the URL using HttpURLConnection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //StringBuilder object to read the string from the service
            StringBuilder sb = new StringBuilder();

            //We will use a buffered reader to read the string from service
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            //A simple string to read values from each line
            String json;

            //reading until we don't find null
            while ((json = bufferedReader.readLine()) != null) {

                //appending it to string builder
                sb.append(json + "\n");
            }

            //finally returning the read string
            return sb.toString().trim();

        } catch (Exception e) {
            return null;
        }
    }
@Override
protected void onPreExecute() {
    alertDialog = new AlertDialog.Builder(context).create();

}

@Override
protected void onPostExecute(String string) {
    alertDialog.setMessage("Complete");
    alertDialog.show();
    SearchActivity.JSONString = string;
}

@Override
protected void onProgressUpdate(Void... values) {
    super.onProgressUpdate(values);
}

}
