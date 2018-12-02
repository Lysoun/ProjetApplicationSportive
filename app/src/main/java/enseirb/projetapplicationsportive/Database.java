package enseirb.projetapplicationsportive;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    /*public long insertRun(Run run){
        ContentValues values = new ContentValues();

        long res = database.insert("runs", null, values);

        Map<Date, Location> path = run.getPath();

        for (Date date: path.keySet()) {
            insertEntry(date, path.get(date));
        }
    }

    private long insertEntry(Date date, Location location){
        ContentValues values = new ContentValues();

        values.put("date", date.toString());
        values.put("longitude", location.getLongitude());
        values.put("latitude", location.getLatitude());

        return database.insert("entries", null, values);
    }*/
}
