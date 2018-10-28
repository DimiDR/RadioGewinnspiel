package com.leafapps.radiogewinnspiel;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    //test of notification
    private Uri notification;
    private Ringtone r;

    private static final String TAG = "MyService";
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
//test of notification
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        //player = MediaPlayer.create(this, );
        //player.setLooping(false); // Set looping
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
        //player.stop();
        r.stop();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
        //player.start();
        r.play();
    }
    
    

}