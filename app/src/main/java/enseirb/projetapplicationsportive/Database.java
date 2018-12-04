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

    public Run getLastRun(long runnerId){
        List<Run> runs = getRuns(runnerId);
        return runs.get(0);
    }

    public List<Run> getRuns(long runnerId){
        String[] columnsRunsQuery = {SQLiteBase.RUN_ID};
        String[] columnsEntriesQuery = {SQLiteBase.ENTRY_TIME, SQLiteBase.ENTRY_LATITUDE, SQLiteBase.ENTRY_LONGITUDE};
        String[] columnsRunnersQuery = {SQLiteBase.USER_NAME};

        String selectionRunsQuery = SQLiteBase.RUN_RUNNER_ID + " = " + runnerId;
        String selectionRunnersQuery = SQLiteBase.USER_ID + " = " + runnerId;

        String orderByRunsQuery = SQLiteBase.RUN_ID + " DESC";
        String orderByEntriesQuery = SQLiteBase.ENTRY_TIME + " DESC";

        // First retrieving all the runs

        Cursor cursorRuns = database.query(true, SQLiteBase.RUNS_TABLE, columnsRunsQuery , selectionRunsQuery,
                null, null, null, orderByRunsQuery, null);
        cursorRuns.moveToFirst();

        // For each run, retrieving its entries
        List<Run> runs = new ArrayList<Run>();

        while(!cursorRuns.isAfterLast()){
            Map<Date, Location> entries = new HashMap<Date, Location>();

            String selectionEntriesQuery = SQLiteBase.ENTRY_RUN_ID + " = " + cursorRuns.getString(0);
            Cursor cursorEntries = database.query(true, SQLiteBase.ENTRIES_TABLE, columnsEntriesQuery, selectionEntriesQuery,
                    null, null, null, orderByEntriesQuery, null);
            cursorEntries.moveToFirst();

            while(!cursorEntries.isAfterLast()){

                try {
                    Date date = new SimpleDateFormat(DATE_PATTERN).parse(cursorEntries.getString(0));
                    // TODO: Retrieve the location!!
                } catch(ParseException e){
                    e.printStackTrace();
                }

                cursorEntries.moveToNext();
            }

            Cursor cursorRunners = database.query(true, SQLiteBase.USERS_TABLE, columnsRunnersQuery, selectionRunnersQuery,
                    null, null, null, null, null);

            cursorRunners.moveToFirst();

            String runnerName = cursorRunners.getString(0);

            runs.add(new Run(entries, runnerName, runnerId));

            cursorRuns.moveToNext();
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

        long runId = database.insert("runs", null, values);

        Map<Date, Location> path = run.getPath();

        for (Date date: path.keySet()) {
            insertEntry(runId, date, path.get(date));
        }

        return runId;
    }

    private long insertEntry(long runId, Date date, Location location){
        ContentValues values = new ContentValues();

        values.put(SQLiteBase.ENTRY_RUN_ID, runId);

        String formatted_date = new SimpleDateFormat(DATE_PATTERN).format(date);
        values.put(SQLiteBase.ENTRY_TIME, formatted_date);

        values.put(SQLiteBase.ENTRY_LONGITUDE, location.getLongitude());

        values.put(SQLiteBase.ENTRY_LATITUDE, location.getLatitude());

        return database.insert(SQLiteBase.ENTRIES_TABLE, null, values);
    }

    public int deleteUser(String name, long userId){
        deleteRuns(userId);
        return database.delete(SQLiteBase.USERS_TABLE, SQLiteBase.USER_NAME + " = " + name, null);
    }

    public int deleteRuns(long userId){
        int res = 0;

        // Look for all the user's runs
        String[] columns = {SQLiteBase.RUN_ID};
        String selection = SQLiteBase.RUN_RUNNER_ID + " = " + userId;

        Cursor cursorRuns = database.query(true, SQLiteBase.RUNS_TABLE, columns, selection,
                null, null, null, null, null);
        cursorRuns.moveToFirst();

        // For each run, delete its entries and then delete the run
        while(!cursorRuns.isAfterLast()) {
            long runId = Long.parseLong(cursorRuns.getString(0));
            deleteEntries(runId);
            res += database.delete(SQLiteBase.RUNS_TABLE, SQLiteBase.RUN_ID + " = " + runId, null);
            cursorRuns.moveToNext();
        }

        return res;
    }

    private int deleteEntries(long runId){
        return database.delete(SQLiteBase.ENTRIES_TABLE, SQLiteBase.ENTRY_RUN_ID + " = " + runId, null);
    }
}
