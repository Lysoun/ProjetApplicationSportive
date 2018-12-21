package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//import android.widget.ArrayAdapter;
import android.widget.EditText;
//import android.widget.ListView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = new Database(this);
        database.open();
    }

    public void goToStart(View view){
        String name = ((EditText) findViewById(R.id.log_name)).getText().toString();

        if(!name.equals("") && database.usersExists(name)) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }
        else {
            ((TextView) findViewById(R.id.login_error)).setText(R.string.log_tv_error);
        }
    }

    public void goToAddUser(View view) {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }

    public void goToDeleteUser(View view) {
        Intent intent = new Intent(this, DeleteUserActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
