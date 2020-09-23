package com.example.plantiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ChartsActivity extends AppCompatActivity {

    private BarChart barChart;
    private static ArrayList<Integer> monthlyTemperature;
    private DatabaseReference reference;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        barChart = findViewById(R.id.BarChart);
        monthlyTemperature = new ArrayList<>();
        final ArrayList<BarEntry> temper = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Temperature");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(dataSnapshot1.exists()){
                        monthlyTemperature.add( Integer.parseInt(dataSnapshot1.getValue().toString()));
                        Log.i("Data", (monthlyTemperature).toString());

                    }
                }

                Log.i("Montly?????", ( Integer.toString(monthlyTemperature.size())));
                for(int i = 0 ; i < monthlyTemperature.size() ; i++){
                    temper.add(new BarEntry(i, monthlyTemperature.get(i)));
                    Log.i("I",monthlyTemperature.get(i).toString());
                }

                BarDataSet barDataSet = new BarDataSet(temper,"Temperature");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(5f);

                BarData barData = new BarData(barDataSet);

                barChart.setFitBars(true);
                barChart.setNoDataText("Loading...");
                barChart.setData(barData);
                barChart.getDescription().setText("Temperature Monthly");
                barChart.animateY(1500);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

}