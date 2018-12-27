package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class DisplayRunActivity extends AppCompatActivity {
    private Database database;
    private ListView runListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_run);

        // TODO: set TextView with run title
        ((TextView) findViewById(R.id.display_tv_run_title)).setText(this.getIntent().getStringExtra("runTitle"));

        /*
        Run run; // TODO: get run from onClickListener in HistoryActivity

        // Set ListView of runs in database
        runListView = (ListView) findViewById(R.id.print_listview);
        List<Location> locationsRun = run.getPath();
        String[] locationsList = new String[locationsRun.size()];

        for(int i = 0; i < locationsRun.size(); i++){
            locationsList[i] = "Latitude : " + locationsRun.get(i).getLatitude() +
                    "\nLongitude : " + locationsRun.get(i).getLongitude() +
                    "\nTemps : " + new Date(locationsRun.get(i).getTime()).toString();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_run_list_view_item, R.id.run_listview_tv, locationsList);
        runListView.setAdapter(arrayAdapter);
        */
    }

    public void goToHistory(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("userId", this.getIntent().getLongExtra("userId", -1));
        startActivity(intent);
    }
}
