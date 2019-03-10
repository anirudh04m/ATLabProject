package com.anirudh.user.getfit;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
//import android.support.v7.app.AppCompatCallback;
//import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.database.sqlite.*;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

     private TextView count ;
     private boolean running = false  ;
     private SensorManager sensor ;
     private float cvalue = 0;
     private Button reset ;
     private DrawerLayout drawerLayout;
     private NavigationView navigationView;
    private AlertDialog alertDialog;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertDialog = new AlertDialog.Builder (MainActivity.this).create();
        // nav drawer code
        // first we add a toolbar
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        // menu item listener in navigation drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (id)
                {
                    case R.id.nav_item_about:

                        alertDialog.setTitle("About");
                        alertDialog.setMessage("Get Fit is a simple pedometer app. The app is completely open-source");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setIcon (android.R.drawable.ic_dialog_alert);
                        alertDialog.show();
                        break;
                    case R.id.nav_item_history:
                        Toast.makeText (getApplicationContext(),"History",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_item_settings:
                        Toast.makeText (getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });

        // sensor buttons and UI controls
        count = (TextView) findViewById(R.id.count);
        sensor = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        reset = (Button) findViewById(R.id.reset);

        // reset button listener
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  (this is for disabling the sensor entirely)
                //sensor.unregisterListener(MainActivity.this);
                cvalue = Float.parseFloat(count.getText().toString());
                count.setText ("0");
                }
            }
        );


    }

    // toolbar toggles nav bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor Count = sensor.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (Count!= null) {
            sensor.registerListener(this,Count,SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this,"Sensor Not Found!" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //running = false ;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running){
            count.setText(String.valueOf(event.values[0] - cvalue));
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm Exit");
                builder.setIcon (R.mipmap.ic_launcher);
                builder.setMessage ("Are you sure you want to exit?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog adialog = builder.create();
        adialog.show();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
