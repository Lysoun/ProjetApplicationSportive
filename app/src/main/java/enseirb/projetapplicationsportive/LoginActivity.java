package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//import android.widget.ArrayAdapter;
import android.widget.EditText;
//import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Database database;

    @Override
    public void onBackPressed() {
        //
        if(!getIntent().getClass().equals(StartActivity.class))
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = new Database(this);
        database.open();
    }

    public void goToStart(View view){
        String login = ((EditText) findViewById(R.id.log_name)).getText().toString();

        if(!login.equals("")) {
            long userId = database.usersExists(login);

            if (userId != -1) {
                Intent intent = new Intent(this, StartActivity.class);
                intent.putExtra("userId", userId);

                Toast.makeText(this, "Bienvenue " + login, Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
            else {
                ((TextView) findViewById(R.id.login_error)).setText(R.string.log_tv_error);
            }
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
