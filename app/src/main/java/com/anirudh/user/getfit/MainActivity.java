package com.anirudh.user.getfit;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

     TextView count ;

     boolean running = false  ;

     SensorManager sensor ;
        float cvalue = 0;
     Button reset ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = (TextView) findViewById(R.id.count);

        sensor = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        reset = (Button) findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         //sensor.unregisterListener(MainActivity.this);
                                         cvalue = Float.parseFloat(count.getText().toString());
                                         count.setText ("0");
                                     }
                                 }
        );


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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
