package com.example.forecastapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.forecastapp.Objects.FiveDayForecastObject;
import com.example.forecastapp.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DailyAdapter extends BaseAdapter {

    List<FiveDayForecastObject> models;
    Context context;

    public DailyAdapter(List<FiveDayForecastObject> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.list_view_item, viewGroup, false);
        TextView temp = view.findViewById(R.id.dailyTempListView);
        ImageView imageView = view.findViewById(R.id.weeklyImageListView);
        TextView dateT = view.findViewById(R.id.dailyDateListView);

        temp.setText(models.get(i).getTemp() + "C°");
        dateT.setText(models.get(i).getDate());
        Picasso.with(context).load("http://openweathermap.org/img/wn/" + models.get(i).getIcon() + "@2x.png").into(imageView);

        return view;
    }
}
