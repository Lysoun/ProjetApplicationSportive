package enseirb.projetapplicationsportive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteBase extends SQLiteOpenHelper {
    public final static String RUNS_TABLE = "Runs";
    public final static String RUN_ID = "id";
    public final static String RUN_RUNNER_ID = "runnerId";

    private final static String CREATE_TABLE_RUNS = "CREATE TABLE " + RUNS_TABLE + "(" +
            RUN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RUN_RUNNER_ID + " INTEGER NOT NULL" +
            "); "
            ;

    public final static String ENTRIES_TABLE = "Entries";
    public final static String ENTRY_ID = "id";
    public final static String ENTRY_RUN_ID = "runId";
    public final static String ENTRY_LATITUDE = "latitude";
    public final static String ENTRY_LONGITUDE = "longitude";
    public final static String ENTRY_TIME = "time";

    private final static String CREATE_TABLE_ENTRIES = "CREATE TABLE " +  ENTRIES_TABLE + "(" +
            ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ENTRY_RUN_ID + " INTEGER NOT NULL, " +
            ENTRY_LATITUDE + " FLOAT NOT NULL, " +
            ENTRY_LONGITUDE + " FLOAT NOT NULL, " +
            ENTRY_TIME + " DATETIME NOT NULL" +
            ")";

    public final static String USERS_TABLE = "Users";
    public final static String USER_ID = "id";
    public final static String USER_NAME = "name";

    private final static String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + "(" +
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_NAME + " TEXT NOT NULL" +
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
        database.delete("runs", null, null);
        createTableRuns(database);
    }

    private void createAllTables(SQLiteDatabase database){
        createTableUsers(database);
        //createTableRuns(database);
        //createTableEntries(database);
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
