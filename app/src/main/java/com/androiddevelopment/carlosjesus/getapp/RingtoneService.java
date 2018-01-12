package com.androiddevelopment.carlosjesus.getapp;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;


public class RingtoneService extends Service {

    public static final String NOTIF_CHANNEL_ID = "my_channel_01";

    MediaPlayer mediaPlayer;
    boolean isPlaying;
    RingtoneManager ringtoneManager;

    Ringtone ring;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotifChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID,
                "MyApp events", NotificationManager.IMPORTANCE_LOW);
        // Configure the notification channel
        channel.setDescription("MyApp event controls");
        channel.setShowBadge(false);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager manager = context.getSystemService(NotificationManager.class);

        manager.createNotificationChannel(channel);
        Log.d("Create the channel", "createNotifChannel: created=" + NOTIF_CHANNEL_ID);
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
        //RingtoneManager rm = new RingtoneManager(RingtoneService.this);
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //All possibilities about the different options when the user press the buttons

        if (intent.getExtras().containsKey("notification")){
            mediaPlayer.stop();
            mediaPlayer.reset();
            v.cancel();
            isPlaying = false;
            flag = false;
        }else {

            if (!isPlaying && flag) {

                mediaPlayer = MediaPlayer.create(this, uri);

                if (sh.getBoolean("notifications_new_message_vibrate", false)) {

                    long[] pattern = {0, 500, 500};

                    v.vibrate(pattern, 0);
                }


                //If the mediaPlayer is null, catch the exception and use a ringtone that is stored in
                //RAW folder.
                try {
                    mediaPlayer.start();
                } catch (NullPointerException e) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.dove);
                    mediaPlayer.start();
                }

                Log.e("Estado alarma", String.valueOf(mediaPlayer.equals(null)));

                //Notification service
                NotificationManager notify = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                //Create an intent which goes to the Alarm class
                Intent intent_alarm = new Intent(this.getApplicationContext(), TurnOffAlarm.class);

                PendingIntent p_intent_alarm = PendingIntent.getActivity(this, 0, intent_alarm, 0);

                //If the API is 26 or greatter, create the channel and use it, else create the notification without the channel

                Notification notification;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotifChannel(this);

                    notification = new Notification.Builder(this)
                            .setContentTitle("An alarm is going off")
                            .setContentText("Click me!")
                            .setContentIntent(p_intent_alarm)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.settings)
                            .setChannelId(NOTIF_CHANNEL_ID)
                            .build();
                } else {
                    notification = new Notification.Builder(this)
                            .setContentTitle("An alarm is going off")
                            .setContentText("Click me!")
                            .setContentIntent(p_intent_alarm)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.settings)
                            .build();
                }

                // Notification call command
                notify.notify(0, notification);

                isPlaying = true;
                flag = false;
            } else if (isPlaying && !flag) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                v.cancel();
                isPlaying = false;
                flag = false;
            } else if (isPlaying && flag) {
                isPlaying = false;
                flag = false;
            } else if (!isPlaying && !flag) {
                isPlaying = true;
                flag = true;
            }
        }




        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.e("On destroy called", "Wake up!!!!");

        super.onDestroy();
        isPlaying = false;
        mediaPlayer.stop();
    }

}