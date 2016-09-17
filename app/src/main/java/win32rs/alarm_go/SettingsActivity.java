package win32rs.alarm_go;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TimePicker;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener{

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private Switch sw;
    private static SettingsActivity inst;

    public static SettingsActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sp = this.getSharedPreferences("win32rs.alarm_go.prefs",this.MODE_PRIVATE);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        sw = (Switch) findViewById(R.id.alarmToggle);
        alarmTimePicker.setCurrentHour(sp.getInt("hour",0));
        alarmTimePicker.setCurrentMinute(sp.getInt("mint",0));
        alarmTimePicker.setOnTimeChangedListener(this);
        sw.setChecked(sp.getBoolean("swch",true));
        //Switch alarmToggle = (Switch) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    public void onToggleClicked(View view) {
        SharedPreferences sp = this.getSharedPreferences("win32rs.alarm_go.prefs",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (((Switch) view).isChecked()) {
            Log.d("MyActivity", "Alarm On");
            setAlarm(alarmTimePicker.getCurrentHour(), alarmTimePicker.getCurrentMinute());
            editor.putBoolean("swch", true);

        } else {
            alarmManager.cancel(pendingIntent);
            //setAlarmText("");
            //Log.d("MyActivity", "Alarm Off");
            editor.putBoolean("swch", false);
        }
        editor.commit();
    }

    public void setAlarm(int h, int m){
        alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);
        Intent myIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, myIntent, 0);
        Log.d("SA","set alarm");
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int h, int m) {
        /*if(sw.isChecked()) {
            setAlarm(h, m);
        }*/
        SharedPreferences sp = this.getSharedPreferences("win32rs.alarm_go.prefs",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("hour",h);
        editor.putInt("mint",m);
        editor.commit();
    }

    //public void setAlarmText(String alarmText) {alarmTextView.setText(alarmText);}
}
