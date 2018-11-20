package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
    }

    public void registerUser(View view){
        EditText editLogin = (EditText)findViewById(R.id.newLogin);
        String login = editLogin.getText().toString();

        Toast.makeText(this, "Bienvenue " + login, Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
