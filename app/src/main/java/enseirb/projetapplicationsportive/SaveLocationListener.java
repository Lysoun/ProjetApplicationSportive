package enseirb.projetapplicationsportive;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SaveLocationListener implements LocationListener {
    private static final Double EPSILON = 0.0000001;
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
        if(path.isEmpty()) {
            Log.i("GpsThread", "Empty path, storing the first location");
            path.add(location);
        } else {
            Location lastLoc = path.get(path.size() - 1);

            if (Math.abs(lastLoc.getLatitude() - location.getLatitude()) >= EPSILON
                    && Math.abs(lastLoc.getLongitude() - location.getLongitude()) >= EPSILON) {
                path.add(location);
                Log.i("GpsThread - locListener", "New location " + location.getLatitude() + " "
                        + location.getLongitude());
            } else {
                Log.i("GpsThread", "Too close to last known location, not stored");
            }
        }
    }
}
