package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrintActivity extends AppCompatActivity {
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        database = new Database(this);
        database.open();

        if(!GpsService.lastRunIsValid()){
            // Displaying an error message
            ((TextView) findViewById(R.id.print_tv_end)).setText(R.string.print_tv_end_error);

            /*// I want to test so let's create an incredible fake run

            List<Location> path = new ArrayList<Location>();
            Location l1 = new Location("test");
            Location l2 = new Location("test");

            l1.setLatitude(0.0);
            l1.setLongitude(0.0);
            l1.setTime(new Date().getTime());

            path.add(l1);

            l2.setLatitude(1.0);
            l2.setLongitude(1.0);
            l2.setTime(new Date().getTime());

            path.add(l2);

            Run r = new Run(path, 0);

            ((TextView) findViewById(R.id.print_tv_run)).setText(r.toString());*/

        }
        else{
            // Displaying the run the user has just done
            Run run = database.getLastRun(this.getIntent().getLongExtra("userId", -1));

            ((TextView) findViewById(R.id.print_tv_run)).setText(run.toString());
        }

    }

    public void goToStart(View view){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        database.close();
    }
}
