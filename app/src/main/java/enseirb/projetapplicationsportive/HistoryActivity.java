package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView runsListView;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        long userId = this.getIntent().getLongExtra("userId", -1);

        database = new Database(this);
        database.open();

        Log.i("GpsThread", "HistoryActivity onCreate()");

        runsListView = (ListView) findViewById(R.id.hist_listview);
        List<Run> runs = database.getRuns(userId);

        Log.i("GpsThread", "HistoryActivity getRuns");

        String[] runsList = new String[runs.size()];

        Log.i("GpsThread", "HistoryActivity runsList");

        for(int i = 0; i < runs.size(); i++){
            runsList[i] = runs.get(i).toString();
        }

        Log.i("GpsThread", "HistoryActivity toString");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_run_list_view_item, R.id.run_listview_tv, runsList);
        runsListView.setAdapter(arrayAdapter);

        Log.i("GpsThread", "HistoryActivity listView done");
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
