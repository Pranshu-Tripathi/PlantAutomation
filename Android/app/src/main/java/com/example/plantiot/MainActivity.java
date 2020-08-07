package com.example.plantiot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private ProgressBar progressBarOnTask;
    private TextView PlantIot;
    private LinearLayout startLinearLayout,stopLinearLayout;

    private DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = findViewById(R.id.nav);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        progressBarOnTask = findViewById(R.id.progressOnTask);
        PlantIot = findViewById(R.id.PlantIot);
        startLinearLayout = findViewById(R.id.startLayout);
        stopLinearLayout = findViewById(R.id.stopLayout);

        progressBarOnTask.setVisibility(View.INVISIBLE);
        reference = FirebaseDatabase.getInstance().getReference();

        startLinearLayout.setOnClickListener(MainActivity.this);
        stopLinearLayout.setOnClickListener(MainActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.temp:
                Toast.makeText(this, "Temperature Selected", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.humidity:
                Toast.makeText(this, "Humidity Selected", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.stats:
                Toast.makeText(this, "Statistics Selected", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View view) {
        if(view == startLinearLayout){
            startWateringFunction();
        }else if(view == stopLinearLayout){
            stopWateringFunction();
        }
    }

    private void startWateringFunction(){
        progressBarOnTask.setVisibility(View.VISIBLE);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    dataSnapshot.getRef().child("start").setValue(1);
                    dataSnapshot.getRef().child("stop").setValue(0);
                }
                Toast.makeText(MainActivity.this, "Starting Watering", Toast.LENGTH_SHORT).show();
                progressBarOnTask.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                progressBarOnTask.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void stopWateringFunction(){

        progressBarOnTask.setVisibility(View.VISIBLE);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    dataSnapshot.getRef().child("start").setValue(0);
                    dataSnapshot.getRef().child("stop").setValue(1);
                }
                Toast.makeText(MainActivity.this, "Stop Watering", Toast.LENGTH_SHORT).show();
                progressBarOnTask.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                progressBarOnTask.setVisibility(View.INVISIBLE);
            }
        });

    }
}