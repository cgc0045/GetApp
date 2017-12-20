package com.androiddevelopment.carlosjesus.getapp;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;



public class RingtoneService extends Service {

    MediaPlayer mediaPlayer;
    boolean isPlaying;

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

        if(!isPlaying && flag){
            mediaPlayer = MediaPlayer.create(this, R.raw.dove);
            mediaPlayer.start();

            isPlaying = true;
            flag = false;
        }else if(isPlaying && !flag){
            mediaPlayer.stop();
            mediaPlayer.reset();

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
    }

}
