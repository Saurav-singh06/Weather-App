package com.practice.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView CityNameTV,tempTV,conditionTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV,iconIV,searchIV;
    private ArrayList<WeatherModel> weatherModelArrayList;
    private WeatherAdapter weatherAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        weatherModelArrayList new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this,weatherModelArrayList);
        weatherRV.setAdapter(weatherAdapter);


    }

    private  void  getWeatherInfo(String cityNmae){

    }
}