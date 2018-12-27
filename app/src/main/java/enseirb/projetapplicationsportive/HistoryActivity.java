package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private static final String DAY = "Jour : ";
    private static final String BEGINNING = "Début : ";
    private static final String END = "Fin : ";

    private ListView runsListView;
    private Database database;

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
}
