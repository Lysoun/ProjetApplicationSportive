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
  //          findViewById(R.id.print_btn_display_run).setVisibility(View.GONE);
            Log.i("GpsThread", "PrintActivity invalid last run --> fake run");
        }
    }

    public void goToDisplayMapActivity(View view){
        Intent intent = new Intent(this, DisplayMapActivity.class);
        intent.putExtra("userId", this.getIntent().getLongExtra("userId", -1));
        startActivity(intent);
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
