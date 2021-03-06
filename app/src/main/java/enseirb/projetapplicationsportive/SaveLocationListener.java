package enseirb.projetapplicationsportive;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SaveLocationListener implements LocationListener {
    private static final long TIMESPAN = 1000;
    private static final Double EPSILON = 0.000000000000005;
    private Location lastLocation;
    private List<Location> path;

    public SaveLocationListener(){
        path = new ArrayList<Location>();
    }

    public List<Location> getPath(){
        return new ArrayList<Location>(path);
    }

    @Override
    public void onLocationChanged(Location location) {
        saveNewLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void saveNewLocation(Location location) {
        if (location != null) {
            if (path.isEmpty()) {
                Log.i("GpsThread", "Empty path, storing the first location");
                path.add(location);
                lastLocation = new Location(location);
            } else {
                if (Math.abs(location.getTime() - lastLocation.getTime()) >= TIMESPAN
                        && Math.abs(location.getLatitude() - lastLocation.getLatitude()) >= EPSILON
                        && Math.abs(location.getLongitude() - lastLocation.getLongitude()) >= EPSILON) {
                    path.add(location);
                    Log.i("GpsThread - locListener", "New location " + location.getLatitude() + " "
                            + location.getLongitude());
                    lastLocation = location;
                } else {
                    Log.i("GpsThread", "Too close in time or space to last known location, not stored");
                }
            }
        }
    }
}
