package enseirb.projetapplicationsportive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

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
    private final static String DATABASE_PROVIDER = "database";
    private SQLiteDatabase database;
    private SQLiteBase mySqliteBase;

    public Database(Context context){
        mySqliteBase = new SQLiteBase(context, "runs", null, VERSION_DATABASE);
    }

    /**
     * Opens the database. You have to call this method before any other
     * method of your instance of Database.
     */
    public void open(){
        database = mySqliteBase.getWritableDatabase();
    }

    /**
     * Closes the database. You have to call this method when you're
     * done using your instance of Database.
     */
    public void close(){
        mySqliteBase.close();
    }

    /**
     * Returns the most recent run the runner has recorded with the application
     * @param runnerId Id of the runner
     * @return The most recent run the runner has recorded with the application
     */
    public Run getLastRun(long runnerId){
        List<Run> runs = getRuns(runnerId);
        return runs.get(0);
    }

    /**
     * Returns all the runs the runner has recorded with the application
     * @param runnerId Id of the runner
     * @return All the runs the runner has recorded with the application
     */
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
        List<Run> runs = new ArrayList<>();

        while(!cursorRuns.isAfterLast()){
            List<Location> entries = new ArrayList<>();

            String selectionEntriesQuery = SQLiteBase.ENTRY_RUN_ID + " = " + cursorRuns.getString(0);
            Cursor cursorEntries = database.query(true, SQLiteBase.ENTRIES_TABLE, columnsEntriesQuery, selectionEntriesQuery,
                    null, null, null, orderByEntriesQuery, null);
            cursorEntries.moveToFirst();

            while(!cursorEntries.isAfterLast()){
                long time = cursorEntries.getLong(0);
                double latitude = cursorEntries.getDouble(1);
                double longitude = cursorEntries.getDouble(2);

                Location location = new Location(DATABASE_PROVIDER);
                location.setTime(time);
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                entries.add(location);

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

    /**
     * Inserts a new user in the database
     * @param name Name of the user to insert
     * @return Id of the user that was just inserted
     */
    public long insertUser(String name){
        ContentValues values = new ContentValues();

        values.put(SQLiteBase.USER_NAME, name);

        return database.insert(SQLiteBase.USERS_TABLE, null, values);
    }

    public String[] getUsers(){
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

        String[] res = new String[users.size()];
        for(int i = 0 ; i < users.size() ; i++){
            res[i] = users.get(i);
        }

        return res;
    }

    /**
     * Checks if a user already exists in the database
     * @param name Name of the user we are looking for
     * @return -1 if the user does not exist, their id if they do
     */
    public long usersExists(String name){
        String[] columns = {SQLiteBase.USER_ID};
        String selection = SQLiteBase.USER_NAME + " = '" + name + "'";

        Cursor cursor = database.query(SQLiteBase.USERS_TABLE, columns, selection, null, null,
                null, null, null);

        if(!cursor.moveToFirst())
            return -1;

        return cursor.getLong(0);
    }
    
    /**
     * Inserts a new run in the database, inserting
     * it and then inserting all its entries
     * @param run The run to insert
     * @return Id of the run that was just inserted
     */
    public long insertRun(Run run){
        ContentValues values = new ContentValues();

        values.put(SQLiteBase.RUN_RUNNER_ID, run.getRunnerId());

        long runId = database.insert(SQLiteBase.RUNS_TABLE, null, values);

        List<Location> path = run.getPath();

        for (Location location : path) {
            Log.w("GpsThread","Inserting path time: " + location.getTime() + " latitude : " + location.getLatitude()
            + " " + location.getLongitude());
            insertEntry(runId, location);
        }

        return runId;
    }

    /**
     * Inserts an entry in the database
     * @param runId The entry's run id
     * @param location The entry's location
     * @return Id of the entry that was just inserted
     */
    private long insertEntry(long runId, Location location){
        ContentValues values = new ContentValues();

        values.put(SQLiteBase.ENTRY_RUN_ID, runId);

        values.put(SQLiteBase.ENTRY_TIME, location.getTime());

        values.put(SQLiteBase.ENTRY_LONGITUDE, location.getLongitude());

        values.put(SQLiteBase.ENTRY_LATITUDE, location.getLatitude());

        return database.insert(SQLiteBase.ENTRIES_TABLE, null, values);
    }

    /**
     * Deletes the user from the database
     * and all their runs
     * @param name Name of the user to delete
     * @param userId Id of the user to delete
     * @return The number of users deleted, 1 if the user was successfully
     * deleted and 0 otherwise.
     */
    public int deleteUser(String name, long userId){
        deleteRuns(userId);
        return database.delete(SQLiteBase.USERS_TABLE, SQLiteBase.USER_NAME + " = " + name, null);
    }

    /**
     * Deletes all the runs recorded by the runner
     * @param runnerId Id of the runner whose runs are to be
     *                 deleted
     * @return The number of runs deleted
     */
    public int deleteRuns(long runnerId){
        int res = 0;

        // Look for all the user's runs
        String[] columns = {SQLiteBase.RUN_ID};
        String selection = SQLiteBase.RUN_RUNNER_ID + " = " + runnerId;

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

    /**
     * Deletes all the entries of the run
     * @param runId Id of the run which entries
     *              are to be deleted
     * @return The number of entries deleted
     */
    private int deleteEntries(long runId){
        return database.delete(SQLiteBase.ENTRIES_TABLE, SQLiteBase.ENTRY_RUN_ID + " = " + runId, null);
    }
}
