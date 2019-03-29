package com.anirudh.user.getfit;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import com.anirudh.user.getfit.ExitDialogFragment;
import com.anirudh.user.getfit.AboutDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    public static final String DATABASE_NAME = "getfitdb.db";
     private TextView count ;
     private boolean running = false  ;
     private SensorManager sensor ;
     private float cvalue = 0;
     private Button reset ;
     private DrawerLayout drawerLayout;
     private NavigationView navigationView;
     private ExitDialogFragment exitDialogFragment;
     private AboutDialogFragment aboutDialogFragment;
     SQLiteDatabase mDatabase;
     Boolean newDay = true;

    public Boolean checkNewDay () {
        Cursor cursorTimeStamp = mDatabase.rawQuery ("SELECT * FROM timestamprecord",null);
        if (! cursorTimeStamp.moveToFirst() )
            return true;
        else
        {
            long timestampnew = cursorTimeStamp.getLong(1);
            if (DateUtils.isToday (timestampnew))
                return false;
            else
                return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // nav drawer code
        // first we add a toolbar
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        exitDialogFragment = ExitDialogFragment.newInstance("Confirm");
        aboutDialogFragment = AboutDialogFragment.newInstance("About");
        mDatabase = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        createDatabase();

        newDay = checkNewDay ();

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
                        showAboutDialog();
                        menuItem.setChecked(false);
                        break;
                    case R.id.nav_item_history:
                        Toast.makeText (getApplicationContext(),"History",Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(false);
                        break;
                    case R.id.nav_item_settings:
                        Toast.makeText (getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        menuItem.setChecked(false);
                        break;
                    default:
                        return true;

                }
                menuItem.setChecked(false);
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
            long boottimestamp = java.lang.System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
            long timemills = boottimestamp + (event.timestamp/1000000l);
            mDatabase.execSQL("DELETE from timestamprecord");
            mDatabase.execSQL ("INSERT INTO timestamprecord VALUES ("+timemills+","+event.values[0]+");");

            Date date = new Date (timemills);
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSSS");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String df = formatter.format (date);

            System.out.println ("Update records:"+df+" "+event.values[0]);
            count.setText(String.valueOf(event.values[0] - cvalue));
        }
    }

    public void showAboutDialog () {
        //AboutDialogFragment aboutDialogFragment = AboutDialogFragment.newInstance ("About");
        aboutDialogFragment.show (getSupportFragmentManager(),"title");
    }
    public void showExitDialog ()
    {
        FragmentManager fm = getSupportFragmentManager();
        //ExitDialogFragment exitDialogFragment = ExitDialogFragment.newInstance ("Confirm");
        exitDialogFragment.show (fm,"title");
    }

    public void createDatabase ()
    {
        mDatabase.execSQL (
                "CREATE TABLE IF NOT EXISTS timestamprecord (\n" +
                        " timestamp int NOT NULL,\n"+
                        " stepcount int NOT NULL\n"+
                        ")");
    }
    @Override
    public void onBackPressed()
    {
        showExitDialog ();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
