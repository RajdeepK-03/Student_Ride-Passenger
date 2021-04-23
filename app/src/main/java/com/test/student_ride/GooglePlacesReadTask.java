package com.test.student_ride;

import android.os.AsyncTask;
import android.util.Log;


public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null,latutude,longitude;

    @Override
    protected String doInBackground(Object... inputObj) {
        try {

            String googlePlacesUrl = (String) inputObj[0];
            latutude = (String) inputObj[1];
            longitude = (String) inputObj[2];
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
        Object[] toPass = new Object[3];
        toPass[0] = result;
        toPass[1] = latutude;
        toPass[2] = longitude;
        placesDisplayTask.execute(toPass);
    }
}