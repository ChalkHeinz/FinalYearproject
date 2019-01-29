package com.example.jackhaines.finalyearproject;

import android.content.Context;
import android.os.AsyncTask;

public class BackendService extends AsyncTask<String, Void, Void> {

    Context context;

    BackendService(Context ctx){
        context = ctx;
    }

    @Override
    protected Void doInBackground(String... voids) {
        String type = voids[0];
        if(type.equals("submit")){
            
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
