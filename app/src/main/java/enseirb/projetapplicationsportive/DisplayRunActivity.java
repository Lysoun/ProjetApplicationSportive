package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        database = new Database(this);
        database.open();

        ((TextView) findViewById(R.id.display_tv_run_title)).setText(this.getIntent().getStringExtra("runTitle"));
    }

    public void goToDisplayMap(View view){
        Intent intent = new Intent(this, DisplayMapActivity.class);
        intent.putExtra("runId", this.getIntent().getLongExtra("runId", -1));
        startActivity(intent);
    }

    public void goToHistory(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("userId", this.getIntent().getLongExtra("userId", -1));
        startActivity(intent);
    }
}
