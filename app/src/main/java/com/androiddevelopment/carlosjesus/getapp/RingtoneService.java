package com.androiddevelopment.carlosjesus.getapp;


import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;



public class RingtoneService extends Service {

    MediaPlayer mediaPlayer;
    boolean isPlaying;
    RingtoneManager ringtoneManager;

    Ringtone ring;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Log.e("In the service", "Yes, the ringtone service");

        //Fetch the extra variable from the intent
        boolean flag = intent.getExtras().getBoolean("extra");

        Log.e("RingtoneService flag: ", String.valueOf(flag));

        /*
        if (flag){
            startId = 1;
        }else if (!flag){
            startId = 0;
        }else{
            startId = 0;
        }*/

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        Uri uri = Uri.parse(sh.getString("notifications_new_message_ringtone", ""));
        ring = RingtoneManager.getRingtone(this, uri);


        if(!isPlaying && flag){
            //mediaPlayer = MediaPlayer.create(this, R.raw.dove);

            Log.e("Ringtone", ring.toString());

            mediaPlayer = MediaPlayer.create(this, uri);
            mediaPlayer.start();
            //ring.play();


            isPlaying = true;
            flag = false;
        }else if(isPlaying && !flag){
            mediaPlayer.stop();
            mediaPlayer.reset();

            //ring.stop();

            isPlaying = false;
            flag = false;
        }else if(isPlaying && flag){
            isPlaying = false;
            flag = false;
        }else if(!isPlaying && !flag){
            isPlaying = true;
            flag = true;
        }




        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.e("On destroy called", "Wake up!!!!");

        super.onDestroy();
        isPlaying = false;
        ring.stop();
    }

}
