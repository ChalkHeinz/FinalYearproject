package com.example.jackhaines.finalyearproject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.net.URLConnection;
import java.net.URLEncoder;

public class BackendService extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;

    BackendService(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String submit_url = "http://35.246.45.206/submit.php";
        if(type.equals("submit")){
            try {
                String googleEmail = params[1];
                String lat = params[2];
                String lon = params[3];
                String species = params[4];
                String image = params[5];


                URL url = new URL(submit_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //Encoded the data here
                String post_data = URLEncoder.encode("GoogleEmail", "UTF-8")+ "=" + URLEncoder.encode(googleEmail, "UTF-8")
                        + "&" + URLEncoder.encode("Lat", "UTF-8")+ "=" + URLEncoder.encode(lat, "UTF-8")
                        + "&" + URLEncoder.encode("Lon", "UTF-8")+ "=" + URLEncoder.encode(lon, "UTF-8")
                        + "&" + URLEncoder.encode("Species", "UTF-8")+ "=" + URLEncoder.encode(species, "UTF-8")
                        + "&" + URLEncoder.encode("Image", "UTF-8")+ "=" + URLEncoder.encode(image, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(type == "get"){
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

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Works?");

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

