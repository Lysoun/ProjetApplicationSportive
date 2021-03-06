package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddUserActivity extends AppCompatActivity {
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new Database(this);
        db.open();

        String[] users = db.getUsers();

        for(String user : users){
            Log.w("users ", user, null);
        }

        setContentView(R.layout.activity_add_user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void addUser(View view) {
        EditText editLogin = (EditText) findViewById(R.id.newLogin);
        String login = editLogin.getText().toString();

        // TODO: Forbid registering with dangerous characters for the database

        if (login.equals(""))
            ((TextView) findViewById(R.id.add_error)).setText(R.string.add_tv_error_no_login);
        else if(db.usersExists(login) != -1)
            ((TextView) findViewById(R.id.add_error)).setText(R.string.add_tv_error_login_exists);
        else {
            Log.w("database returns", db.insertUser(login) + "", null);

            long userId = db.usersExists(login);

            Toast.makeText(this, "Bienvenue " + login, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, StartActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
