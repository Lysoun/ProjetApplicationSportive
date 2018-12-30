package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private static final String DAY = "Jour : ";
    private static final String BEGINNING = "Début : ";
    private static final String END = "Fin : ";

    private ListView runsListView;
    private Database database;

    // TODO: Fix bug including 2 runs in history after one run

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        long userId = this.getIntent().getLongExtra("userId", -1);

        database = new Database(this);
        database.open();

        runsListView = (ListView) findViewById(R.id.hist_listview);

        List<Run> runs = database.getRuns(userId);

        if(runs.size() > 0) {
            String[] runsList = new String[runs.size()];

            for (int i = 0; i < runs.size(); i++) {
                runsList[i] = runs.get(i).getRunListViewDisplay();
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_run_list_view_item, R.id.run_listview_tv, runsList);
            runsListView.setAdapter(arrayAdapter);

            // Set listener to access locations of a run on click
            runsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    accessRunLocations(view, position); // id and position seem to be equal
                }
            });
        } else{
            ((TextView) findViewById(R.id.hist_tv)).setVisibility(View.VISIBLE);
        }
    }

    public void goToStart(View view){
        Intent intent = new Intent(this, StartActivity.class);
        intent.putExtra("userId", this.getIntent().getLongExtra("userId", -1));
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        database.close();
    }

    private void accessRunLocations(View view, int position){
        String runTitle = ((TextView) (view.findViewById(R.id.run_listview_tv))).getText().toString();

        Intent intent = new Intent(this, DisplayRunActivity.class);
        intent.putExtra("userId", this.getIntent().getLongExtra("userId", -1));
        intent.putExtra("runPosition", position);
        intent.putExtra("runTitle", runTitle);
        startActivity(intent);
    }
}
