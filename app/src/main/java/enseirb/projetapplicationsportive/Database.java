package enseirb.projetapplicationsportive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private final static int VERSION_DATABASE = 1;
    private final static String DATE_PATTERN = "YYYY-MM-DD HH:MM:SS.SSS";
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

    public Run getLastRun(long runner_id){
        List<Run> runs = getRuns(runner_id);
        return runs.get(0);
    }

    public List<Run> getRuns(long runner_id){
        String[] columns_runs_query = {SQLiteBase.RUN_ID};
        String[] columns_entries_query = {SQLiteBase.ENTRY_TIME, SQLiteBase.ENTRY_LATITUDE, SQLiteBase.ENTRY_LONGITUDE};
        String[] columns_runners_query = {SQLiteBase.USER_NAME};

        String selection_runs_query = SQLiteBase.RUN_RUNNER_ID + " = " + runner_id;
        String selection_runners_query = SQLiteBase.USER_ID + " = " + runner_id;

        String order_by_runs_query = SQLiteBase.RUN_ID + " DESC";
        String order_by_entries_query = SQLiteBase.ENTRY_TIME + " DESC";

        // First retrieving all the runs

        Cursor cursor_runs = database.query(true, SQLiteBase.RUNS_TABLE, columns_runs_query , selection_runs_query,
                null, null, null, order_by_runs_query, null);

        // For each run, retrieving its entries
        List<Run> runs = new ArrayList<Run>();

        while(!cursor_runs.isAfterLast()){
            Map<Date, Location> entries = new HashMap<Date, Location>();

            String selection_entries_query = SQLiteBase.ENTRY_RUN_ID + " = " + cursor_runs.getString(0);
            Cursor cursor_entries = database.query(true, SQLiteBase.ENTRIES_TABLE, columns_entries_query, selection_entries_query,
                    null, null, null, order_by_entries_query, null);

            while(!cursor_entries.isAfterLast()){


                try {
                    Date date = new SimpleDateFormat(DATE_PATTERN).parse(cursor_entries.getString(0));
                    // TODO: Retrieve the location!!
                } catch(ParseException e){
                    e.printStackTrace();
                }

                cursor_entries.moveToNext();
            }

            Cursor cursor_runners = database.query(true, SQLiteBase.USERS_TABLE, columns_runners_query, selection_runners_query,
                    null, null, null, null, null);

            String runner_name = cursor_runners.getString(0);

            runs.add(new Run(entries, runner_name, runner_id));

            cursor_runs.moveToNext();
        }

        return runs;
    }

    public long insertUser(String name){
        ContentValues values = new ContentValues();

        values.put(SQLiteBase.USER_NAME, name);

        return database.insert(SQLiteBase.USERS_TABLE, null, values);
    }

    public long insertRun(Run run){
        ContentValues values = new ContentValues();

        values.put(SQLiteBase.RUN_RUNNER_ID, run.getRunnerId());

        long run_id = database.insert("runs", null, values);

        Map<Date, Location> path = run.getPath();

        for (Date date: path.keySet()) {
            insertEntry(run_id, date, path.get(date));
        }

        return run_id;
    }

    private long insertEntry(long run_id, Date date, Location location){
        ContentValues values = new ContentValues();

        values.put(SQLiteBase.ENTRY_RUN_ID, run_id);

        String formatted_date = new SimpleDateFormat(DATE_PATTERN).format(date);
        values.put(SQLiteBase.ENTRY_TIME, formatted_date);

        values.put(SQLiteBase.ENTRY_LONGITUDE, location.getLongitude());

        values.put(SQLiteBase.ENTRY_LATITUDE, location.getLatitude());

        return database.insert(SQLiteBase.ENTRIES_TABLE, null, values);
    }

    public int deleteUser(long user_id){
        return database.delete(SQLiteBase.USERS_TABLE, SQLiteBase.USER_ID + " = " + user_id, null);
    }
}
