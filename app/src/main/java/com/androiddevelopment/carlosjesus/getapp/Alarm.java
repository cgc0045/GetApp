package com.androiddevelopment.carlosjesus.getapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {

    AlarmManager alarmManager;
    TimePicker timePicker;
    TextView updateText;
    Context context;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        this.context = this;

        //Initialize the alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Initialize the time picker
        timePicker = (TimePicker) findViewById(R.id.selectHour);

        updateText = (TextView) findViewById(R.id.updateText);

        final Calendar calendar = Calendar.getInstance();

        //Initialize the buttons
        Button startAlarm = (Button) findViewById(R.id.alarmOn);
        Button finishAlarm = (Button) findViewById(R.id.alarmOff);

        //Intent to AlarmReceiver
        final Intent arIntent = new Intent(this.context, AlarmReceiver.class);

        //Create the OnClickListener for each button
        startAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);



                set_alarm_text("Alarm on! - " + hour + ":" + (minute < 10 ? "0" + minute : minute));

                //Extra boolean variable to indicates when the user press the 'Alarm on' button
                arIntent.putExtra("extra", true);

                //Pending Intent to delay the alarm respect the time selected
                pendingIntent = PendingIntent.getBroadcast(Alarm.this, 0, arIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                //Set the correct configuration into the Alarm Manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });

        finishAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If the user set the alarm previously, it's
                // possible to cancel it;
                if (arIntent.hasExtra("extra")) {
                    set_alarm_text("Alarm off!");

                    //Cancel the alarm intent
                    alarmManager.cancel(pendingIntent);

                    //Extra boolean variable to indicates when the user press the 'Alarm off' button
                    arIntent.putExtra("extra", false);

                    //Stop the alarm
                    sendBroadcast(arIntent);
                }
            }
        });
    }

    private void set_alarm_text(String s){
        updateText.setText(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.settings) {
            //return true;
            //lo ideal aquí sería hacer un intent para abrir una nueva clase como lo siguiente
            Log.i("ActionBar", "Settings!");
            Intent about = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
