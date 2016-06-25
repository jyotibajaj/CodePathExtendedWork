package com.letsdecode.mytodo.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.letsdecode.mytodo.models.TaskDetail;

import java.util.ArrayList;


public class SQLiteDataAdapter {
    static SQLiteDataBaseHelper sqLiteDataBaseHelper;

    /*
     just so we can have a reference of object of inner class, in order to call getWritableDatabase method and get a
     reference of SQlite database object.
      */
    private Context context;

    public SQLiteDataAdapter(Context context) {
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(context);
        this.context = context;
    }
    static void print(ArrayList<TaskDetail> toprint) {
        for (TaskDetail i : toprint) {
            Log.d(SQLiteDataAdapter.class.getSimpleName(), " item " + i.toString());
        }
    }


    //method to insert data(item name and quantity) from input box to data box
    public static void updateItemData(int id, String name, String date, String priority) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDataBaseHelper.TASK_NAME, name);
        contentValues.put(SQLiteDataBaseHelper.DUE_DATE, date);
        contentValues.put(SQLiteDataBaseHelper.PRIORITY, priority);
        SQLiteDatabase dbObject;
        dbObject = sqLiteDataBaseHelper.getWritableDatabase();
        dbObject.update(SQLiteDataBaseHelper.TABLE_ITEM, contentValues, "item_id=?", new String[]{"" + id});
    }

    public static void setItemDone(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDataBaseHelper.STATUS, "done");
        SQLiteDatabase dbObject;
        dbObject = sqLiteDataBaseHelper.getWritableDatabase();
        dbObject.update(SQLiteDataBaseHelper.TABLE_ITEM, contentValues, "item_id=?", new String[]{"" + id});
    }

    public static void setItemNotDone(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDataBaseHelper.STATUS, "todo");
        SQLiteDatabase dbObject;
        dbObject = sqLiteDataBaseHelper.getWritableDatabase();
        dbObject.update(SQLiteDataBaseHelper.TABLE_ITEM, contentValues, "item_id=?", new String[]{"" + id});
    }

    //method to insert data(item name and quantity) from input box to data box
    public static boolean deleteItemData(int id) {
        String[] columns = {sqLiteDataBaseHelper.UID, sqLiteDataBaseHelper.TASK_NAME, SQLiteDataBaseHelper.DUE_DATE, sqLiteDataBaseHelper.PRIORITY, sqLiteDataBaseHelper.STATUS};
        SQLiteDatabase dbObject;
        dbObject = sqLiteDataBaseHelper.getWritableDatabase();
        return dbObject.delete(SQLiteDataBaseHelper.TABLE_ITEM, SQLiteDataBaseHelper.UID + "=" + id, null) > 0;
    }


    //method to insert data(item name and quantity) from input box to data box
    public static long insertItemData(String itemName, String date, String priority) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDataBaseHelper.TASK_NAME, itemName);
        contentValues.put(SQLiteDataBaseHelper.DUE_DATE, date);
        contentValues.put(SQLiteDataBaseHelper.PRIORITY, priority);
        contentValues.put(SQLiteDataBaseHelper.STATUS, "todo");
        SQLiteDatabase dbObject;
        dbObject = sqLiteDataBaseHelper.getWritableDatabase();
        long id = dbObject.insert(SQLiteDataBaseHelper.TABLE_ITEM, null, contentValues);
        return id;
    }


    public static ArrayList<TaskDetail> getToDoItemData() {
        SQLiteDatabase dbObject;
        dbObject = sqLiteDataBaseHelper.getWritableDatabase();
        String[] columns = {sqLiteDataBaseHelper.UID, sqLiteDataBaseHelper.TASK_NAME, SQLiteDataBaseHelper.DUE_DATE, sqLiteDataBaseHelper.PRIORITY, sqLiteDataBaseHelper.STATUS};
        /* public Cursor query( String table, String[] columns,
        String selection, String[] selectionArgs, String groupBy,
                String having, String orderBy) {
                */
        /* return cursor type object and this cursor object contains the subset of table containing the result.
        cursor is used to retrieve data and we use query method of SQliteDatabase
         */
        Cursor cursor = dbObject.query(sqLiteDataBaseHelper.TABLE_ITEM, columns, null, null, null, null, null);
        ArrayList<TaskDetail> itemArrayList = new ArrayList<TaskDetail>();
        // when reach end of the statement moveNxt will return false and execution of loop stops.
        //cursor steps up row wise, it complete one row and then moves to next.
        while (cursor.moveToNext()) {
            int uidIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.UID);
            int taskIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.TASK_NAME);
            int timeIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.DUE_DATE);
            int priorityIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.PRIORITY);
            int statusIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.STATUS);
            int uid = cursor.getInt(uidIndex);
            String itemName = cursor.getString(taskIndex);
            String time = cursor.getString(timeIndex);

            String priority = cursor.getString(priorityIndex);
            String status = cursor.getString(statusIndex);
            itemArrayList.add(new TaskDetail(itemName, time, priority, status, uid));

        }
        return itemArrayList;


    }

    public static TaskDetail getItemByID(String id){
        SQLiteDatabase dbObject;
        TaskDetail itemObject = null;
        dbObject = sqLiteDataBaseHelper.getWritableDatabase();
        String[] columns = {sqLiteDataBaseHelper.UID, sqLiteDataBaseHelper.TASK_NAME, SQLiteDataBaseHelper.DUE_DATE, sqLiteDataBaseHelper.PRIORITY, sqLiteDataBaseHelper.STATUS};
        /* public Cursor query( String table, String[] columns,
        String selection, String[] selectionArgs, String groupBy,
                String having, String orderBy) {
                */
        /* return cursor type object and this cursor object contains the subset of table containing the result.
        cursor is used to retrieve data and we use query method of SQliteDatabase
         */
        Cursor cursor = dbObject.query(sqLiteDataBaseHelper.TABLE_ITEM, columns, "item_id=?", new String[]{id}, null, null, null);
        ArrayList<TaskDetail> itemArrayList = new ArrayList<TaskDetail>();
        // when reach end of the statement moveNxt will return false and execution of loop stops.
        //cursor steps up row wise, it complete one row and then moves to next.
        while (cursor.moveToNext()) {
            int uidIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.UID);
            int taskIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.TASK_NAME);
            int timeIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.DUE_DATE);
            int priorityIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.PRIORITY);
            int statusIndex = cursor.getColumnIndex(sqLiteDataBaseHelper.STATUS);
            int uid = cursor.getInt(uidIndex);
            String itemName = cursor.getString(taskIndex);
            String time = cursor.getString(timeIndex);

            String priority = cursor.getString(priorityIndex);
            String status = cursor.getString(statusIndex);
            itemObject = (new TaskDetail(itemName, time, priority, status, uid));

        }
        return itemObject;


    }




    // inner class so that its private variable can only be accessible to outer class
    static class SQLiteDataBaseHelper extends SQLiteOpenHelper {
        /*
        This class talks about creating schema eg: database name, table name, columns names etc.
         */
        private static final int DATABASE_VERSION = 3;
        private static final String DATABASE_NAME = "userInformationDataBase";


        //item info table and columns
        private static final String TABLE_ITEM = "itemInformationTable";
        private static final String UID = "item_id";
        private static final String TASK_NAME = "taskName";
        private static final String DUE_DATE = "dueDate";
        private static final String STATUS = "status";
        private static final String PRIORITY = "priority";


        private static final String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS " + TABLE_ITEM;


        private Context context;

        private static final String CREATE_TABLE_ITEM = "CREATE TABLE " + TABLE_ITEM + "("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK_NAME + " VARCHAR(255), " +
                DUE_DATE + " VARCHAR(20), " + PRIORITY + " VARCHAR(255), " + STATUS + " VARCHAR(255)) ";

        // constructor
        public SQLiteDataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }


        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            // toast message to get info if onCreate is called
            try {
                db.execSQL(CREATE_TABLE_ITEM);
            } catch (SQLException e) {
                Log.d("exception on create", "" + e);


            }
//            init();
        }

        //test cases
//        public void init() {
//            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date date = new Date();
//            String currentDate = dateFormat.format(date);
//
//
//            //Test Queries
//            insertItemData("item 1", date, "low");
//            insertItemData("item 2", date, "high");
//            insertItemData("item 3", date, "urgent");
//            insertItemData("item 4", date, "medium");
//
//
//            Log.d(SQLiteDataAdapter.class.getSimpleName(), " To DO Items Start =========");
//            ArrayList<TaskDetail> todo = getToDoItemData();
//            print(todo);
//            Log.d(SQLiteDataAdapter.class.getSimpleName(), " To DO Items End =========");
//
//            updateItemData("1", "item 1 done", "" + date.getTime(), "low");
//            updateItemData("2", "item 2 done", "" + date.getTime(), "urgent");
//            updateItemData("3", "item 3 done", "" + date.getTime(), "medium");
//
//            Log.d(SQLiteDataAdapter.class.getSimpleName(), " To DO Items after done  Start =========");
//            todo = getToDoItemData();
//            print(todo);
//            Log.d(SQLiteDataAdapter.class.getSimpleName(), " To DO Items after done End =========");
//
//
//            Log.d(SQLiteDataAdapter.class.getSimpleName(), " Done Items Start =========");
//            ArrayList<TaskDetail> done = getDoneItemData();
//            print(done);
//            Log.d(SQLiteDataAdapter.class.getSimpleName(), " Done Items End =========");
//
//
//        }
//

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // Drop older table if existed
            try {
                db.execSQL(DROP_ITEM_TABLE);
                // Create tables again
                onCreate(db);
            } catch (SQLException e) {
            }


        }


    }
}
