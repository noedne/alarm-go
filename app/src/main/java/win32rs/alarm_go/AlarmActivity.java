package win32rs.alarm_go;

import android.app.Activity;
import android.content.ComponentName;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity{
    MediaPlayer mp;
    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //this will update the UI with message
        SettingsActivity inst = SettingsActivity.instance();
        inst.setAlarmText("Alarm! Wake up! Waaake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        /*Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();*/
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        mp= MediaPlayer.create(this, alarmUri);
        mp.setLooping(true);
        mp.setVolume(1,1);
        mp.start();
        final TextView timer = (TextView) findViewById(R.id.timer);
        timer.setText("Shh.");
        cdt = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(""+ millisUntilFinished / 1000);
            }

            public void onFinish() {
                mp.start();
                timer.setText("Shh.");
            }
        };
    }
    public void timeTapped(View view){
        cdt.cancel();
        cdt.start();
        mp.pause();

    }

}
