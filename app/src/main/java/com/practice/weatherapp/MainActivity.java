package com.practice.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView CityNameTV,tempTV,conditionTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV,iconIV,searchIV;
    private ArrayList<WeatherModel> weatherModelArrayList;
    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        homeRL = findViewById(R.id.rl_Home);
        loadingPB = findViewById(R.id.pb_loading);
        CityNameTV = findViewById(R.id.txt_CityName);
        tempTV = findViewById(R.id.txt_Temp);
        conditionTV = findViewById(R.id.txt_Condion);
        weatherRV = findViewById(R.id.Rv_Weather);
        cityEdt = findViewById(R.id.et_City);
        backIV = findViewById(R.id.img_Back);
        iconIV = findViewById(R.id.img_Icon);
        searchIV = findViewById(R.id.img_search);

        weatherModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this,weatherModelArrayList);
        weatherRV.setAdapter(weatherAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(),location.getLatitude());

        getWeatherInfo(cityName);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityEdt.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter city Name",Toast.LENGTH_SHORT).show();
                }else {
                    CityNameTV.setText(cityName);
                    getWeatherInfo(city);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permissions granted..",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Please Provide the permissions ",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName (double longitude , double latitude){
        String cityName ="Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);

            for (Address adr : addresses){
                if (adr != null){
                    String city = adr.getLocality();
                    if (city!=null && !city.equals("")){
                        cityName = city;
                    }else {
                        Log.d("Tag","City Not Found");
                        Toast.makeText(this,"User City Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return cityName;
    }

    private  void  getWeatherInfo(String cityName){
    String url = "http://api.weatherapi.com/v1/forecast.json?key=13b2dd0fb522481e9d0183358223101&q="+cityName+"&days=1&aqi=no&alerts=no";
    CityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                weatherModelArrayList.clear();
                try {
                    String temp = response.getJSONObject("current").getString("temp_c");
                    tempTV.setText(temp + "°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");

                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionICon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionICon)).into(iconIV);
                    conditionTV.setText(condition);
                    if (isDay ==1){
                        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtd8rODjFXHXH9_FdzULV9TmzodjNZnMNNmnPJT7HIQBlb0R10b9c4_66OsU5rHIATm5s&usqp=CAU").into(backIV);
                    }else {
                        Picasso.get().load("https://media.istockphoto.com/vectors/weather-app-design-modern-ui-screen-design-for-mobile-app-with-web-vector-id1178256163?k=20&m=1178256163&s=170667a&w=0&h=0pHKXAK2RNx7RWGviPiaQ7uK3aV1vOjLYFJH6aCdcuE=").into(backIV);
                    }

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forcast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forcast0.getJSONArray("hour");

                    for (int i=0; i<hourArray.length(); i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherModelArrayList.add(new WeatherModel(time,temper,img,wind));
                    }
                    weatherAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Please enter Valid city name..",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}