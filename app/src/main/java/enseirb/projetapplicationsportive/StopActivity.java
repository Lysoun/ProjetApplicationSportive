package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
    }

    public void goToPrint(View view){
        stopService(new Intent(StopActivity.this, GpsService.class));

        Intent intent = new Intent(this, PrintActivity.class);
        startActivity(intent);
    }
}