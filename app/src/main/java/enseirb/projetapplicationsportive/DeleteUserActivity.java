package enseirb.projetapplicationsportive;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
    private ListView userListView;
    private String[] userList;
    private final static String CONFIRM = "Confirmation";
    private final static String DELETED_USER = "Utilisateur supprimé ";

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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                builder.setTitle(CONFIRM)
                        .setMessage("Voulez vous vraiment supprimer l'utilisateur "
                                + userList[(int) id] + " et toutes ses courses ?")
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes,
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        deleteUser(view);
                                    }
                                }).create().show();
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

        long userId = db.usersExists(login);

        if (userId != -1){ // The else case shouldn't be possible given the selection in the ListView
            Toast.makeText(this, DELETED_USER + ": " + login, Toast.LENGTH_SHORT).show();
            db.deleteUser(userId);
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
