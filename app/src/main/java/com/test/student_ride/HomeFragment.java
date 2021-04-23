package com.test.student_ride;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HomeFragment extends Fragment implements LocationListener {

    EditText txt_zip;
    Button btn_search;
    int PROXIMITY_RADIUS =2*1000;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    double latitude, longitude;

    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    String current_latitude,current_longitude,destination_lat,destination_lon;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btn_search=root.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.showLoader(getActivity(),"Please wait . . . ");
                new zip_code_location().execute("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyDPEhcdpxjNkWVWUbzgVSGKJluStRICll0&components=postal_code:"+txt_zip.getText().toString().trim());

            }
        });
        txt_zip=root.findViewById(R.id.txt_zip);
        txt_zip.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
                Pattern pattern = Pattern.compile(regex);

                Matcher matcher = pattern.matcher(txt_zip.getText().toString().toUpperCase());
                if(matcher.matches()){
                    btn_search.setVisibility(View.VISIBLE);
                }
                else{
                    btn_search.setVisibility(View.INVISIBLE);
                }


            }
        });
        return root;
    }



    public class zip_code_location extends AsyncTask<String, String, String> {
        String res=null;
        @Override
        protected String doInBackground(String... parms) {
            Http http=new Http();
            try {
                res= http.read(parms[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }



        @Override
        protected void onPostExecute(String response) {
            List<HashMap<String, String>> googlePlacesList = null;


            try
            {

                Zip_LatLong zip_latLong=new Zip_LatLong();
                JSONObject jsonObject = new JSONObject(response);
                googlePlacesList=   zip_latLong.parse(jsonObject);
                 destination_lat=    googlePlacesList.get(0).get("lat");
                 destination_lon=    googlePlacesList.get(0).get("lng");

                DatabaseHelper databaseHelper=new DatabaseHelper(MyApplication.getAppContext());
                if(  databaseHelper.addcoordinate(String.valueOf(destination_lat),String.valueOf(destination_lon))){

                   // Toast.makeText(MyApplication.getAppContext(),"Coordinates registered !",Toast.LENGTH_LONG).show();
                //    GPSTracker gpsTracker = new GPSTracker(getActivity());

             //      current_latitude= String.valueOf( gpsTracker.latitude);
               //      current_longitude=  String.valueOf(gpsTracker.longitude);



                }
                Helper.stopLoader();

                getLocation();
                /*

                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlacesUrl.append("&types=" + "police");
                googlePlacesUrl.append("&sensor=true");
                googlePlacesUrl.append("&key=" + "AIzaSyDPEhcdpxjNkWVWUbzgVSGKJluStRICll0");

                GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                Object[] toPass = new Object[3];
                toPass[0] = googlePlacesUrl.toString();
                toPass[1] = String.valueOf(latitude);
                toPass[2] = String.valueOf(longitude);
                googlePlacesReadTask.execute(toPass);

                 */

            }
            catch (Exception e){
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();

            }
        }
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("Detect","Location changed");
        locationManager.removeUpdates(this);
        current_latitude = String.valueOf(location.getLatitude());
        current_longitude = String.valueOf(location.getLongitude());

        Toast.makeText(getActivity(),"Latitude"+current_latitude+" , Longitude"+current_longitude,Toast.LENGTH_LONG).show();

        if(current_latitude.equals("") || current_longitude.equals("")){
            Toast.makeText(getActivity(),"Current location not found !",Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(getActivity(), activity_ride_details.class);
            intent.putExtra("destination", txt_zip.getText().toString());
            intent.putExtra("current_lat", current_latitude);
            intent.putExtra("current_lon", current_longitude);
            intent.putExtra("destination_lat", destination_lat);
            intent.putExtra("destination_lon", destination_lon);
            startActivity(intent);
        }
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {

            current_latitude = String.valueOf(gps.getLatitude());
            current_longitude = String.valueOf(gps.getLongitude());
            Log.d("lat", "" + latitude);
            Log.d("long", "" + longitude);

            Intent intent = new Intent(getActivity(), activity_ride_details.class);
            intent.putExtra("destination", txt_zip.getText().toString());
            intent.putExtra("current_lat", current_latitude);
            intent.putExtra("current_lon", current_longitude);
            intent.putExtra("destination_lat", destination_lat);
            intent.putExtra("destination_lon", destination_lon);
            startActivity(intent);


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}
