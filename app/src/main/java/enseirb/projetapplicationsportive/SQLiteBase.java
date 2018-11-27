package enseirb.projetapplicationsportive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteBase extends SQLiteOpenHelper {
    private final static String CREATE_TABLE_RUNS = "CREATE TABLE Runs(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "runnerId INTEGER NOT NULL, " +
            "); "
            ;

    private final static String CREATE_TABLE_ENTRIES = "CREATE TABLE Entries(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "runId INTEGER NOT NULL, " +
            "latitude FLOAT NOT NULL, " +
            "longitude FLOAT NOT NULL, " +
            "time DATETIME NOT NULL, " +
            ")";

    private final static String CREATE_TABLE_USERS = "CREATE TABLE Users(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            ")"
            ;

    public SQLiteBase(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        createAllTables(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.delete("books", null, null);
        createTableRuns(database);
    }

    private void createAllTables(SQLiteDatabase database){
        createTableUsers(database);
        createTableRuns(database);
        createTableEntries(database);
    }

    private void createTableRuns(SQLiteDatabase database){
        createTable(CREATE_TABLE_RUNS, database);
    }

    private void createTableUsers(SQLiteDatabase database){
        createTable(CREATE_TABLE_USERS, database);
    }

    private void createTableEntries(SQLiteDatabase database){
        createTable(CREATE_TABLE_ENTRIES, database);
    }

    private void createTable(String name, SQLiteDatabase database){
        database.execSQL(name);
    }
}
