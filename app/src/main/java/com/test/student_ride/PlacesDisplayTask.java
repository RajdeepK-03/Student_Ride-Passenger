package com.test.student_ride;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    String lati,longi;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {


            googlePlacesJson = new JSONObject((String) inputObj[0]);
            lati = (String) inputObj[1];
            longi = (String) inputObj[2];
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        DatabaseHelper databaseHelper=new DatabaseHelper(MyApplication.getAppContext());
        if(  databaseHelper.addcoordinate(String.valueOf(lati),String.valueOf(longi))){

            Toast.makeText(MyApplication.getAppContext(),"Coordinates registered !",Toast.LENGTH_LONG).show();


            Helper.stopLoader();
        }

    }



}

