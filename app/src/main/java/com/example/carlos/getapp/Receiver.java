package com.example.carlos.getapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by carlos on 1/11/17.
 */

public class Receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, Ringtone.class);

        context.startService(service);

    }
}
