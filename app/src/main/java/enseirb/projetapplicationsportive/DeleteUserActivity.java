package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class DeleteUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Array of strings...
        ListView simpleList;
        String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};

        simpleList = (ListView)findViewById(R.id.del_listview);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_delete_user, R.id.listview_tv, countryList);
        simpleList.setAdapter(arrayAdapter);

        setContentView(R.layout.activity_delete_user);
    }

    public void deleteUser(View view) {
       /* EditText editLogin = (EditText) findViewById(R.id.del_login);
        String login = editLogin.getText().toString();

        // TODO: Print list of user logins
        // TODO: Check if user exists

        Toast.makeText(this, "Utilisateur supprimé : " + login, Toast.LENGTH_SHORT).show();
        */

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
