package com.example.carlos.getapp;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by carlos on 1/11/17.
 */

public class Ringtone extends Service {
    MediaPlayer song;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        song = MediaPlayer.create(this, R.raw.entredosmares);
        song.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this, "on destroy called", Toast.LENGTH_SHORT).show();
    }
}
