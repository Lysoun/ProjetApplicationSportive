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
            ENTRY_TIME + " TEXT NOT NULL, " +
            ")";

    public final static String RUNNERS = "Runners";
    public final static String RUNNER_ID = "id";
    public final static String RUNNER_NAME = "name";

    private final static String CREATE_TABLE_USERS = "CREATE TABLE " + RUNNERS + "(" +
            RUNNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RUNNER_NAME + " TEXT NOT NULL, " +
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
        deleteAllTables(database);
        createAllTables(database);
    }

    private void createAllTables(SQLiteDatabase database){
        createTableUsers(database);
        createTableRuns(database);
        createTableEntries(database);
    }

    private void deleteAllTables(SQLiteDatabase database){
        deleteTableEntries(database);
        deleteTableRuns(database);
        deleteTableUsers(database);
    }

    private void createTableRuns(SQLiteDatabase database){
        createTable(CREATE_TABLE_RUNS, database);
    }

    private void deleteTableRuns(SQLiteDatabase database){
        deleteTable(RUNS_TABLE, database);
    }

    private void createTableUsers(SQLiteDatabase database){
        createTable(CREATE_TABLE_USERS, database);
    }

    private void deleteTableUsers(SQLiteDatabase database){
        deleteTable(RUNNERS, database);
    }

    private void createTableEntries(SQLiteDatabase database){
        createTable(CREATE_TABLE_ENTRIES, database);
    }

    private void deleteTableEntries(SQLiteDatabase database){
        deleteTable(ENTRIES_TABLE, database);
    }

    private void createTable(String name, SQLiteDatabase database){
        database.execSQL(name);
    }

    private void deleteTable(String name, SQLiteDatabase database){ database.delete(name, null, null); }
}
