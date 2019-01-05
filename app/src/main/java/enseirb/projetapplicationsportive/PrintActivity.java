package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrintActivity extends AppCompatActivity {
    private Database database;
    private ListView runListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        long userId = this.getIntent().getLongExtra("userId", -1);

        database = new Database(this);
        database.open();

        Run run;

        if(!GpsService.lastRunIsValid()){
            // Displaying an error message
            ((TextView) findViewById(R.id.print_tv_end)).setText(R.string.print_tv_end_error);
            Log.i("GpsThread", "PrintActivity invalid last run --> fake run");
        }
        else {
            // The run the user has just done
            run = database.getLastRun(userId);

            ((TextView) findViewById(R.id.display_tv_stats)).setText(run.getStats());

            // Set ListView of runs in database
            runListView = (ListView) findViewById(R.id.print_listview);
            List<Location> locationsRun = run.getPath();
            String[] locationsList = new String[locationsRun.size()];

            for (int i = 0; i < locationsRun.size(); i++) {
                locationsList[i] = "Latitude : " + locationsRun.get(i).getLatitude() +
                        "\nLongitude : " + locationsRun.get(i).getLongitude() +
                        "\nTemps : " + new Date(locationsRun.get(i).getTime()).toString();
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_run_list_view_item, R.id.run_listview_tv, locationsList);
            runListView.setAdapter(arrayAdapter);
        }
    }

    public void goToStart(View view){
        Intent intent = new Intent(this, StartActivity.class);
        intent.putExtra("userId", this.getIntent().getLongExtra("userId", -1));
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
