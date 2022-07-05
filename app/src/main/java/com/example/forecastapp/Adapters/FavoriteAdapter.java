package com.example.forecastapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.forecastapp.DatabaseModels.FavoriteModel;
import com.example.forecastapp.MainActivity;
import com.example.forecastapp.R;

import java.util.List;

import io.realm.Realm;

public class FavoriteAdapter extends BaseAdapter {

    Realm realm = Realm.getDefaultInstance();
    List<FavoriteModel> models;
    Context context;

    public FavoriteAdapter(List<FavoriteModel> models, Context context) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.alert_list_view_item, viewGroup, false);

        CardView cardView = view.findViewById(R.id.alertListItemCardV);
        TextView cityname = view.findViewById(R.id.alertCityText);
        ImageButton delete = view.findViewById(R.id.deleteFavoriteAlert);

        cityname.setText(models.get(i).getCity());
        String city = models.get(i).getCity();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("cityname", city);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //date is primary key

                        FavoriteModel model = realm.where(FavoriteModel.class).equalTo("date", models.get(i).getDate()).findFirst();
                        model.deleteFromRealm();
                        Toast.makeText(context.getApplicationContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        });

        return view;
    }
}
