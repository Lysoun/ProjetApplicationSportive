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
    }

    public void goToStart(View view){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void newUser(View view) {
        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }
}
