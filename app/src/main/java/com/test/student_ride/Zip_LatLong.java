package com.test.student_ride;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Zip_LatLong {

    public List<HashMap<String, String>> parse(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getCordinates(jsonArray);
    }

    private List<HashMap<String, String>> getCordinates(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> cordinate = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> _cordinate = null;

        for (int i = 0; i < placesCount; i++) {
            try {
                _cordinate = getcordinate((JSONObject) jsonArray.get(i));
                cordinate.add(_cordinate);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cordinate;
    }

    private HashMap<String, String> getcordinate(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String latitude = "";
        String longitude = "";
        try {

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}