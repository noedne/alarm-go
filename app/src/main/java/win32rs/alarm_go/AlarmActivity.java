package win32rs.alarm_go;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.content.pm.PackageManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.drive.*;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity
        /*implements OnConnectionFailedListener*/ {
    private GoogleApiClient mGoogleApiClient;
    MediaPlayer mp;
    Location mLastLocation, mCurrentLocation;
    LocationSettingsRequest.Builder builder;
    PendingResult<LocationSettingsResult> result;

    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity * /,
                        this /* OnConnectionFailedListener * /)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .build();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //this will update the UI with message
        //SettingsActivity inst = SettingsActivity.instance();
        //inst.setAlarmText("Alarm! Wake up! Waaake up!");


        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        mp= MediaPlayer.create(this, alarmUri);
        mp.setLooping(true);
        mp.setVolume(1,1);
        mp.start();
        final TextView timer = (TextView) findViewById(R.id.timer);
        //timer.setText("Shh.");
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

    protected void onStart() {
//        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onConnected(Bundle connectionHint) {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( AlarmActivity.this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
            return;
        mLastLocation = mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    protected void getLocation() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( AlarmActivity.this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
            return;
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation == null)
            mCurrentLocation = mLastLocation;
    }

    protected float checkDistance() {
        getLocation();
        return mLastLocation.distanceTo(mCurrentLocation);
    }

    protected void onStop() {
//        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void timeTapped(View view){
        mp.pause();
        //Log.d("geo",""+checkDistance());
        cdt.cancel();
        cdt.start();
    }

    public void onConnectionFailedListener(ConnectionResult result) {

    }

    //@Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // ...

    }

}
