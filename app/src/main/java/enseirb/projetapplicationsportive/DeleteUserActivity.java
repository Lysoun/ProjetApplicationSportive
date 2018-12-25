package enseirb.projetapplicationsportive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteUserActivity extends AppCompatActivity {
    private Database db;
    ListView userListView;
    String[] userList;

    // TODO: Fix crash on click on user name
    // TODO: delete user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new Database(this);
        db.open();

        userList = db.getUsers();

        setContentView(R.layout.activity_delete_user);

        // Set ListView of users in database
        userListView = (ListView) findViewById(R.id.del_listview);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_user_list_view_item, R.id.listview_tv, userList);
        userListView.setAdapter(arrayAdapter);

        // Set listener to delete user on click
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteUser(view);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void deleteUser(View view) {
        String login = ((TextView) (view.findViewById(R.id.listview_tv))).getText().toString();

        // TODO: Suppr user
        long userId = db.usersExists(login);

        if (userId != -1){ // The else case shouldn't be possible given the selection in the ListView
            Toast.makeText(this, "Utilisateur à supprimer : " + login + " " + userId, Toast.LENGTH_SHORT).show();
            db.deleteUser(login, userId);
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
