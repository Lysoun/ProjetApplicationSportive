package enseirb.projetapplicationsportive;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class DisplayMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private List<Location> path;
    private Database database;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database = new Database(this);
        database.open();

        long runId = this.getIntent().getLongExtra("runId", -1);
        long userId = this.getIntent().getLongExtra("userId", -1);

        if(runId == -1){
            // Getting the last run
            path = database.getLastRun(userId).getPath();
        } else{
            // Getting this run
            path = database.getRun(runId).getPath();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlgn = null;
        LatLngBounds latLngBounds = null;

        // Adding a marker for each position
        for(int i = 0; i < path.size(); i++){
            Location location = path.get(i);
            latlgn = new LatLng(location.getLatitude(), location.getLongitude());

            if(latLngBounds == null)
                latLngBounds = new LatLngBounds(latlgn, latlgn);
            else
                latLngBounds = latLngBounds.including(latlgn);

            mMap.addMarker(new MarkerOptions().position(latlgn).title("Position " + (path.size() - i)));
        }

        // Moving the camera so that we can see all the run
        int width = this.getWindowManager().getDefaultDisplay().getWidth();
        int height = this.getWindowManager().getDefaultDisplay().getHeight();

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,width, height, 50));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
