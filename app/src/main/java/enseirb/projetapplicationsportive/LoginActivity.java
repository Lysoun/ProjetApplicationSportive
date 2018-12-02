package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: Print list of user logins
    }

    public void goToStart(View view){
        // TODO: Check if user exists

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
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
