package enseirb.projetapplicationsportive;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class GpsThread extends Thread implements Runnable {
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean flag = false;
    public Handler handler;

    public GpsThread(Context context, LocationManager locationManager, LocationListener locationListener){
        this.context = context;
        this.locationManager = locationManager;
        this.locationListener = locationListener;
    }

    @Override
    public void run() {

        Looper.prepare();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
            }
        };

        while (!Thread.interrupted()) {
            try {
                if (context != null) {
                    flag = displayGpsStatus();
                    if (flag
                            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        try {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        } catch (IllegalArgumentException e) {
                            e.getStackTrace();
                            Log.w("GpsThread - catch", "illegal argument --> locationManager or locationListener is null");
                        } catch (SecurityException e) {
                            e.getStackTrace();
                            Log.w("GpsThread - catch", "no suitable permission");
                        } catch (RuntimeException e) {
                            e.getStackTrace();
                            Log.w("GpsThread - catch", "runtime error -->  calling thread has no Looper");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("GpsThread - exception", "run failed, interrupting thread");
                return;
            }
        }
    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = context.getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;
        } else {
            return false;
        }
    }
}