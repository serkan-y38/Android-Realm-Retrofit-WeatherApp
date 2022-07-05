package com.example.forecastapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forecastapp.Objects.HourlyObject;
import com.example.forecastapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    List<HourlyObject> models;
    Context context;

    public HourlyAdapter(List<HourlyObject> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rcycl_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyObject model = models.get(position);
        Picasso.with(context).load("https:" + model.getIcon()).into(holder.conditionImg);
        holder.degreeT.setText(model.getDegree() + " C°");

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");

        try {
            Date d = input.parse(model.getTime());
            holder.timeT.setText(output.format(d));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeT;
        TextView degreeT;
        ImageView conditionImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeT = itemView.findViewById(R.id.hourlyTimeRcycleItemT);
            conditionImg = itemView.findViewById(R.id.hourlyImageRcycleItemT);
            degreeT = itemView.findViewById(R.id.hourlyDegreeRcycleItemT);
        }
    }
}
