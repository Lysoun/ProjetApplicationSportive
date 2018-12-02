package enseirb.projetapplicationsportive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final static int VERSION_DATABASE = 1;
    private SQLiteDatabase database;
    private SQLiteBase mySqliteBase;

    public Database(Context context){
        mySqliteBase = new SQLiteBase(context, "runs", null, VERSION_DATABASE);
    }

    public void open(){
        database = mySqliteBase.getWritableDatabase();
    }

    public void close(){
        mySqliteBase.close();
    }

    public long insertUser(String name){
        ContentValues values = new ContentValues();

        values.put(SQLiteBase.USER_NAME, name);

        return database.insert(SQLiteBase.USERS_TABLE, null, values);
    }

    public List<String> getUsers(){
        String[] columns = {SQLiteBase.USER_NAME};
        String order_by = SQLiteBase.USER_NAME + " ASC";
        Cursor cursor = database.query(SQLiteBase.USERS_TABLE, columns, null, null, null,
                null, order_by, null);

        List<String> users = new ArrayList<>();

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            users.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return users;
    }

    /*public long insertRun(Run run){
        ContentValues values = new ContentValues();

        long res = database.insert("runs", null, values);

        Map<Date, Location> path = run.getPath();

        for (Date date: path.keySet()) {
            insertEntry(date, path.get(date));
        }
    }*/

    /*private long insertEntry(Date date, Location location){
        ContentValues values = new ContentValues();

        values.put("date", date.toString());
        values.put("longitude", location.getLongitude());
        values.put("latitude", location.getLatitude());

        return database.insert("entries", null, values);
    }*/
}
