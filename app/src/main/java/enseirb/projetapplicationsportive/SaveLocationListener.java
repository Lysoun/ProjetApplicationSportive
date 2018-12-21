package enseirb.projetapplicationsportive;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SaveLocationListener implements LocationListener {
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
        path.add(location);
        Log.w("GpsThread", "New location " + location.getLongitude() + " "
                + location.getLatitude());
    }
}
