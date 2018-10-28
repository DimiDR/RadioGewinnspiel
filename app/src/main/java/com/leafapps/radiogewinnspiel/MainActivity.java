package com.leafapps.radiogewinnspiel;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

import com.leafapps.radiogewinnspiel.AlarmReceiver;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    //Phone Number 1Live
    String phoneNumber;
    //Play sound alarm if Song was found
    Uri alarm;
    Ringtone r;

    //trigger for start Button
    boolean trigger;

    //last 3 songs in the playlist. They are implemented in the linear layout vertical.
    public static  TextView TVSong1;
    public static TextView TVSong2;
    public static TextView TVSong3;

    public static TextView TVSong1time;
    public static TextView TVSong2time;
    public static TextView TVSong3time;

    public  EditText etsong;
    //Set and cancel the Alarm
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private AlarmReceiver alarmReceiver;
    private Vibrator vibrator;
    private boolean popupopen;
    private int id=123678;

    //Service Variables
    private static final String TAG = "Service";
    //how often the songs will be checked
    private int interval = 1000;

    //Buttons
    Button trackButton;
    Button callButton;

    //Context
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Phone number 1Live
        phoneNumber = "+498005678111";
        popupopen=false;
        mContext = this;
        //Ringtone Alarm
        alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), alarm);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmReceiver = new AlarmReceiver();
        //alarmReceiver.setActivity(this); //TODO: check
        //Buttons
        trackButton = (Button) findViewById(R.id.trackButton);
        trackButton.setOnClickListener(this);

        callButton = (Button) findViewById(R.id.callButton);
        callButton.setOnClickListener(this);

        //Compare String set Context for Toast
        LevenshteinDistance.setContext(this);

        //Text Views
        TVSong1 = (TextView) findViewById(R.id.linearLayoutSong1);
        TVSong1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (LevenshteinDistance.CompareStrings(etsong.getText().toString(),
                        TVSong1.getText().toString())==1) {
                    callpopup(etsong.getText().toString(), TVSong1.getText().toString());
                    cancelAlarm();
                   // Intent alarmIntent =
                }
            }
        });


        TVSong2 = (TextView) findViewById(R.id.linearLayoutSong2);
        TVSong3 = (TextView) findViewById(R.id.linearLayoutSong3);

        TVSong1time = (TextView) findViewById(R.id.linearLayoutSong1time);
        TVSong2time = (TextView) findViewById(R.id.linearLayoutSong2time);
        TVSong3time = (TextView) findViewById(R.id.linearLayoutSong3time);
        //Edittext
        etsong = (EditText) findViewById(R.id.editText);

        trigger = true;

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        checkFirstRun();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Alarm Starter
    //Documentations: http://www.sitepoint.com/scheduling-background-tasks-android/
    public void startAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        //Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    //Cancel the Alarm
    public void cancelAlarm() {
        if (manager != null) {
            manager.cancel(pendingIntent);
            //Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == trackButton.getId()) {
            //new Title().execute();
            //Toast.makeText(this, "I'm running", Toast.LENGTH_SHORT).show();
            if (trigger) {
                //alarmReceiver.ExecuteBackgroundProcess();
                if ( etsong.getText().toString().isEmpty() ) {
                    Toast.makeText(this, "Geben Sie einen Text zum suchen ein.", Toast.LENGTH_SHORT).show();
                }
                else {
                    trackButton.setText("Überwachung Abbrechen");
                    startAlarm();
                    Alarm(false);
                    popupopen = false;
                    //  Log.d(TAG, "onClick: starting service");
                    //  startService(new Intent(this, MyService.class));
                    trigger = false;
                    showNotification();
                }
            } else {
                trackButton.setText("SUCHEN");
                cancelAlarm();
                Alarm(false);
                //  Log.d(TAG, "onClick: stopping service");
                //  stopService(new Intent(this, MyService.class));
                trigger = true;
            }
        }
        if (v.getId() == callButton.getId()){
            //this is a test of the alert dialog
            //it will be implemented in the call the radio station button
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        }
    }


public static void settext(String str1, String str2, String str3){
    TVSong1.setText(str1);
    TVSong2.setText(str2);
    TVSong3.setText(str3);
}
public static void settime(String str1, String str2, String str3){
    //richtige edittext ..
        TVSong1time.setText(str1);
        TVSong2time.setText(str2);
        TVSong3time.setText(str3);
    }





//Diese function sorgt dafür dass die App als Notification angezeigt wird
    public void showNotification() {


        Intent resultIntent = this.getIntent();//new Intent(this, MainActivity.class);
        //PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0); //beim anklicken wird die Mainactivity gestartet
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
       // PendingIntent pi = PendingIntent.getActivity(this,0,new Intent(this, MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)        //Bild, das als Notification gezeigt wird
                //.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(r.getString(R.string.notification_title))    //Titel in der Notification
                .setContentText("Letzter Song:"+TVSong1.getText())
                .setContentIntent(pi)
                .setDeleteIntent(pi)
                .setAutoCancel(true)

                //.addAction(R.drawable.ic_action_search, "Reply", pi)
                //.addAction(android.R.drawable.ic_media_pause, "cancel", pi) //TODO Stop Textur einbauen
                //.addAction(R.drawable.settings, "setings", pi)
                //.setCategory(Notification.CATEGORY_ALARM)
                // Müsste die notification-kategorie beim anklicken sein
                .setContentIntent(resultPendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }
//Diese function checkt ob die app das erste mal gestartet wird, falls ja, wird ein toast angezeigt
//funktioniert noch nicht. kein fehler, aber die meldung kommt nicht, könnte am emulator liegen
    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            // Place your dialog code here to display the dialog
            //Toast.makeText(this, "erster Programmstart", Toast.LENGTH_LONG).show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", true) //true setzen wenn der einmalige screen immer angezeigt werden soll
                    .apply();

            //go to Tutorial activity
            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);

        }

    }

    public void callpopup(String string1, String string2){
        if(!popupopen) {
            popupopen=true;
            //play Alarm
            Alarm(true);
            //stop tracking of the radio station
            trackButton.setText("SUCHEN");
            trigger = true;
            cancelAlarm();

            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
            builder1.setMessage("Der Suchbegriff " + string1 + " wurde gefunden in " + string2);
            builder1.setCancelable(false);
            builder1.setPositiveButton("Anrufen",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Alarm(false);
                            //call the radio station
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
                            //close dialog

                            dialog.cancel();
                        }
                    });
            builder1.setNegativeButton("Abbrechen",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Alarm(false);
                            //close dialog

                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Alarm(false);
    }

    private void Alarm(boolean OnOff) {
        if (OnOff) {
            r.play();
            vibrator.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, -1);
        }
        else{
            r.stop();
            vibrator.cancel();
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);


    }


    @Override
    protected void onResume() {
        super.onResume();
        Alarm(false);
    }


}

