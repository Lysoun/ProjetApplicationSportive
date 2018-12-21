package enseirb.projetapplicationsportive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class StartActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void startRunning(View view){
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            Log.w("GpsThread - main", "getCoordinates() --> authorisation denied");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {

            // start service
            Intent intent = new Intent(StartActivity.this, GpsService.class);
            intent.putExtra("userId", this.getIntent().getLongExtra("userId", -1));
            startService(intent);

            // go to StopActivity
            goToStop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    if (locationAccepted) {
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                    }
                }
                break;
            default:
                return;
        }
    }

    private void goToStop(){
        Intent intent = new Intent(this, StopActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
