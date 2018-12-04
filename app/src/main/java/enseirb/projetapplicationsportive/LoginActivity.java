package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: Print list of user logins
    }

    public void goToStart(View view){
        // TODO: Check if user exists
        String name = ((EditText) findViewById(R.id.name)).getText().toString();

        Database database = new Database(this);
        database.open();

        if(!name.equals("") && database.usersExists(name)) {
            database.close();
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }
        else{
            database.close();
            Log.i("login", "non");
            ((TextView) findViewById(R.id.error)).setText(R.string.log_tv_error);
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
}
