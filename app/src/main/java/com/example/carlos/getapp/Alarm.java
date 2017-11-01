package com.example.carlos.getapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {

    //Create the alarm manager
    AlarmManager alarmMan;
    TimePicker alarmPick;
    Context context;

    CharSequence setText = "Alarm has been set at ";
    CharSequence turnOffText = "Alarm has been turned off";

    PendingIntent pIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        this.context = this;

        // Initialize the variables

        alarmPick = (TimePicker) findViewById(R.id.timePicker);
        alarmMan = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Calendar calendar = Calendar.getInstance();

        Button onButton = (Button) findViewById(R.id.set);
        Button offButton = (Button) findViewById(R.id.end);

        final Intent intent = new Intent(context, Receiver.class);

        // Override the method when we click on the buttons

        onButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY, alarmPick.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmPick.getCurrentMinute());

                String minute;

                //Add a 0 if the minute is less than 10
                if (alarmPick.getCurrentMinute() < 10){
                    minute = "0" + alarmPick.getCurrentMinute().toString();
                }else{
                    minute = alarmPick.getCurrentMinute().toString();
                }


                show_message(setText + alarmPick.getCurrentHour().toString() + ":" + minute);

                pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmMan.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_message(turnOffText);

                alarmMan.cancel(pIntent);
            }
        });
    }

    /**
     * This method show a message. This message is showed when we turn on or turn off the alarm.
     *
     * @param mess
     */
    private void show_message(CharSequence mess){
        Toast toast = Toast.makeText(context, mess, Toast.LENGTH_SHORT);
        toast.show();
    }
}
