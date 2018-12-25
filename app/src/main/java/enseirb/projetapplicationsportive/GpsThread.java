package enseirb.projetapplicationsportive;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.List;

public class GpsThread implements Runnable{
    private Context context;
    private LocationManager locationManager;
    private SaveLocationListener locationListener;
    private boolean flag = false;
    public Handler handler;
    private Database database;
    private long runnerId;

    public GpsThread(Context context, LocationManager locationManager, SaveLocationListener locationListener, long runnerId){
        this.context = context;
        this.locationManager = locationManager;
        this.locationListener = locationListener;
        this.runnerId = runnerId;
        database = new Database(context);
        Log.i("GpsThread", "GpsThread created");
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

        /* Since we close the database inside the run() and we may call it again without the
       GpsThread being destroyed, we need the open() method here and not in the constructor. */
        database.open();

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
                            e.printStackTrace();
                            Log.i("GpsThread - catch", "illegal argument --> locationManager or locationListener is null");
                        } catch (SecurityException e) {
                            e.printStackTrace();
                            Log.i("GpsThread - catch", "no suitable permission");
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            Log.i("GpsThread - catch", "runtime error -->  calling thread has no Looper");
                        }

                        if (locationManager != null) {
                            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Log.i("GpsThread - no GPS", "calling getLastKnownLocation() = [ " + loc.getLatitude() + " | " + loc.getLongitude() + " ]");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("GpsThread - exception", "run failed, interrupting thread");
                return;
            }
        }

        // When the thread is interrupted, the run is done so let's save it
        // in the database!
        Log.i("GpsThread", "Thread interrupted! Let's insert the run!");
        List<Location> path = locationListener.getPath();

        if(path != null) {
            database.insertRun(new Run(path, runnerId));
        }

        database.close();
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

    public boolean isEmptyPath(){
        return locationListener.getPath().isEmpty();
    }
}