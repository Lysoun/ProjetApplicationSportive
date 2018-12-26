package enseirb.projetapplicationsportive;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class GpsService extends Service {
    private Context mContext;
    static private GpsThread gpsThread = null;
    static private Thread thread = null;
    private LocationManager locationManager = null;
    private SaveLocationListener locationListener = null;
    private Run run = null;
    private long userId = -1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand (Intent intent,int flags, int startId){
        userId = intent.getLongExtra("userId", -1);
        mContext = getApplicationContext();

        if(locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

        if(locationListener == null){
            locationListener = new SaveLocationListener();
        }

        if (thread == null) {
            gpsThread = new GpsThread(mContext, locationManager, locationListener, userId);
        }
        if(thread == null) {
            thread = new Thread(gpsThread);
        }
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy () {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }

        if(thread != null) {
            thread.interrupt();
        }

        thread = null;
        locationManager = null;
        locationListener = null;

        super.onDestroy();
    }

    public static boolean lastRunIsValid(){
        return gpsThread.getPathSize() > 1;
    }
}
