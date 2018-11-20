package enseirb.projetapplicationsportive;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;

public class GpsService extends Service {
    private Context mContext;
    static private Thread gpsThread = null;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private static Location location = null;
    private static Double latitude = new Double(0);
    private static Double longitude = new Double(0);

    public GpsService() {
    }

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
        mContext = getApplicationContext();

        if (gpsThread == null) {
            gpsThread = new Thread(new GpsThread(mContext, locationManager, locationListener));
            gpsThread.start();
        }

        if(!gpsThread.isAlive()) {
            gpsThread.start();
        }

        if(locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

        if(locationListener == null){
            locationListener = new SaveLocationListener();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy () {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }

        if(gpsThread != null) {
            gpsThread.interrupt();
        }

        gpsThread = null;
        locationManager = null;
        location = null;
        latitude = 0.;
        longitude = 0.;

        super.onDestroy();
    }
}
