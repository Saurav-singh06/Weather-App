package com.practice.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
   private Context context;

    public WeatherAdapter(Context context, ArrayList<WeatherModel> weatherModelArrayList) {
        this.context = context;
        this.weatherModelArrayList = weatherModelArrayList;
    }

    private ArrayList<WeatherModel> weatherModelArrayList;

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_weather_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {

        WeatherModel model = weatherModelArrayList.get(position);
       holder.temperatureTV.setText(model.getTemp() + "°C");
       Picasso.get().load("http:".concat(model.getIcon())).into(holder.conditionTV);
       holder.windTV.setText(model.getWindSpeed()+"km/h");
       SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
       SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
       try{
           Date t = input.parse(model.getTime());
           holder.timeTV.setText(output.format(t));
       }catch (ParseException e) {
           e.printStackTrace();
       }
    }

    @Override
    public int getItemCount() {
        return  weatherModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView windTV,temperatureTV,timeTV;
        private ImageView conditionTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTV = itemView.findViewById(R.id.txtWindSpeed);
            temperatureTV = itemView.findViewById(R.id.txtTemperature);
            timeTV = itemView.findViewById(R.id.txtTime);
            conditionTV = itemView.findViewById(R.id.imgCondition);

        }
    }
}
