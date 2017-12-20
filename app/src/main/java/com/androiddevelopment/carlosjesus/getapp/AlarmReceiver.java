package com.androiddevelopment.carlosjesus.getapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver", "Yay!");

        //Fetch the extra variable from the intent
        boolean flag = intent.getExtras().getBoolean("extra");

        Log.e("Key received: ", String.valueOf(flag));

        //Intent to ringtone service
        Intent ringIntent = new Intent(context, RingtoneService.class);

        //Pass the extra variable to the Ringtone Service
        ringIntent.putExtra("extra", flag);

        //Start the service
        context.startService(ringIntent);
    }
}
