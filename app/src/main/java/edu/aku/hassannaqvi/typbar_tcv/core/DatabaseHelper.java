package edu.aku.hassannaqvi.typbar_tcv.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.aku.hassannaqvi.typbar_tcv.contracts.ChildContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.ChildContract.FormsChildTable;
import edu.aku.hassannaqvi.typbar_tcv.contracts.EnrollmentContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.EnrollmentContract.EnrollChildTable;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract.FormsTable;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract.singleSchool;
import edu.aku.hassannaqvi.typbar_tcv.contracts.TehsilsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.TehsilsContract.TehsilsTable;
import edu.aku.hassannaqvi.typbar_tcv.contracts.UCsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.UCsContract.UCsTable;
import edu.aku.hassannaqvi.typbar_tcv.contracts.UsersContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.UsersContract.UsersTable;

/**
 * Created by hassan.naqvi on 11/30/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String SQL_CREATE_USERS = "CREATE TABLE " + UsersContract.UsersTable.TABLE_NAME + "("
            + UsersTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UsersTable.ROW_USERNAME + " TEXT,"
            + UsersTable.ROW_PASSWORD + " TEXT,"
            + UsersTable.ROW_TEAM + " TEXT"
            + " );";
    public static final String DATABASE_NAME = "typbar_tcv.db";
    public static final String DB_NAME = DATABASE_NAME.replace(".", "_copy.");
    public static final String PROJECT_NAME = "DMU-TYPBAR-TCV";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_FORMS = "CREATE TABLE "
            + FormsTable.TABLE_NAME + "("
            + FormsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FormsTable.COLUMN_PROJECT_NAME + " TEXT,"
            + FormsTable.COLUMN_UID + " TEXT," +
            FormsTable.COLUMN_FORMDATE + " TEXT," +
            FormsTable.COLUMN_USER + " TEXT," +
            //FormsChildTable.COLUMN_FORMTYPE + " TEXT," +
            FormsTable.COLUMN_SA + " TEXT," +
            FormsTable.COLUMN_SB + " TEXT," +
            FormsTable.COLUMN_SC + " TEXT," +
            FormsTable.COLUMN_SD + " TEXT," +
            FormsTable.COLUMN_ISTATUS + " TEXT," +
            FormsTable.COLUMN_ISTATUS88x + " TEXT," +
            FormsTable.COLUMN_GPSLAT + " TEXT," +
            FormsTable.COLUMN_GPSLNG + " TEXT," +
            FormsTable.COLUMN_GPSDATE + " TEXT," +
            FormsTable.COLUMN_GPSACC + " TEXT," +
            FormsTable.COLUMN_DEVICEID + " TEXT," +
            FormsTable.COLUMN_DEVICETAGID + " TEXT," +
            FormsTable.COLUMN_APP_VERSION + " TEXT," +
            FormsTable.COLUMN_SYNCED + " TEXT," +
            FormsTable.COLUMN_SYNCED_DATE + " TEXT"
            + " );";


    private static final String SQL_CREATE_CHILD_FORMS = "CREATE TABLE "
            + FormsChildTable.TABLE_NAME + "("
            + FormsChildTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FormsChildTable.COLUMN_PROJECT_NAME + " TEXT,"
            + FormsChildTable.COLUMN_UID + " TEXT," +
            FormsChildTable.COLUMN_UUID + " TEXT," +
            FormsChildTable.COLUMN_FORMDATE + " TEXT," +
            FormsChildTable.COLUMN_USER + " TEXT," +
            FormsChildTable.COLUMN_SB + " TEXT," +
            FormsChildTable.COLUMN_ISTATUS + " TEXT," +
            FormsChildTable.COLUMN_GPSLAT + " TEXT," +
            FormsChildTable.COLUMN_GPSLNG + " TEXT," +
            FormsChildTable.COLUMN_GPSDATE + " TEXT," +
            FormsChildTable.COLUMN_GPSACC + " TEXT," +
            FormsChildTable.COLUMN_DEVICEID + " TEXT," +
            FormsChildTable.COLUMN_DEVICETAGID + " TEXT," +
            FormsChildTable.COLUMN_APP_VERSION + " TEXT," +
            FormsChildTable.COLUMN_SYNCED + " TEXT," +
            FormsChildTable.COLUMN_SYNCED_DATE + " TEXT"
            + " );";


    private static final String SQL_CREATE_ENROLLMENT_FORMS = "CREATE TABLE "
            + EnrollChildTable.TABLE_NAME + "("
            + EnrollChildTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EnrollChildTable.COLUMN_PROJECT_NAME + " TEXT,"
            + EnrollChildTable.COLUMN_UID + " TEXT," +
            EnrollChildTable.COLUMN_UUID + " TEXT," +
            EnrollChildTable.COLUMN_FORMDATE + " TEXT," +
            EnrollChildTable.COLUMN_USER + " TEXT," +
            EnrollChildTable.COLUMN_SC + " TEXT," +
            EnrollChildTable.COLUMN_ISTATUS + " TEXT," +
            EnrollChildTable.COLUMN_GPSLAT + " TEXT," +
            EnrollChildTable.COLUMN_GPSLNG + " TEXT," +
            EnrollChildTable.COLUMN_GPSDATE + " TEXT," +
            EnrollChildTable.COLUMN_GPSACC + " TEXT," +
            EnrollChildTable.COLUMN_DEVICEID + " TEXT," +
            EnrollChildTable.COLUMN_DEVICETAGID + " TEXT," +
            EnrollChildTable.COLUMN_APP_VERSION + " TEXT," +
            EnrollChildTable.COLUMN_SYNCED + " TEXT," +
            EnrollChildTable.COLUMN_SYNCED_DATE + " TEXT"
            + " );";


    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + UsersContract.UsersTable.TABLE_NAME;
    private static final String SQL_DELETE_FORMS =
            "DROP TABLE IF EXISTS " + FormsTable.TABLE_NAME;

    private static final String SQL_DELETE_CHILD_FORMS =
            "DROP TABLE IF EXISTS " + FormsChildTable.TABLE_NAME;

    private static final String SQL_DELETE_ENROLLMENT_FORMS =
            "DROP TABLE IF EXISTS " + EnrollChildTable.TABLE_NAME;


    private static final String SQL_DELETE_SINGLE = "DROP TABLE IF EXISTS " + singleSchool.TABLE_NAME;

    final String SQL_CREATE_SERIAL = "CREATE TABLE " + singleSchool.TABLE_NAME + " (" +
            singleSchool._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            singleSchool.COLUMN_DEVICE_ID + " TEXT, " +
            singleSchool.COLUMN_DATE + " TEXT, " +
            singleSchool.COLUMN_SERIAL_NO + " TEXT, " +
            singleSchool.COLUMN_SYNCED + " TEXT, " +
            singleSchool.COLUMN_SYNCED_DATE + " TEXT " +
            ");";

    private static final String SQL_DELETE_TALUKAS = "DROP TABLE IF EXISTS " + TehsilsTable.TABLE_NAME;
    private static final String SQL_DELETE_UCS = "DROP TABLE IF EXISTS " + UCsTable.TABLE_NAME;

    final String SQL_CREATE_TALUKA = "CREATE TABLE " + TehsilsTable.TABLE_NAME + " (" +
            TehsilsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TehsilsTable.COLUMN_TALUKA_CODE + " TEXT, " +
            TehsilsTable.COLUMN_TALUKA_NAME + " TEXT " +
            ");";
    final String SQL_CREATE_UC = "CREATE TABLE " + UCsTable.TABLE_NAME + " (" +
            UCsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            UCsTable.COLUMN_UCCODE + " TEXT, " +
            UCsTable.COLUMN_UCS_NAME + " TEXT, " +
            UCsTable.COLUMN_TALUKA_CODE + " TEXT " +
            ");";

    private final String TAG = "DatabaseHelper";


    public String spDateT = new SimpleDateFormat("dd-MM-yy").format(new Date().getTime());


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_FORMS);
        db.execSQL(SQL_CREATE_SERIAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_FORMS);
        db.execSQL(SQL_DELETE_CHILD_FORMS);
        db.execSQL(SQL_DELETE_ENROLLMENT_FORMS);
        db.execSQL(SQL_DELETE_SINGLE);
        db.execSQL(SQL_DELETE_TALUKAS);
        db.execSQL(SQL_DELETE_UCS);
    }

    public void syncTehsils(JSONArray Talukaslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TehsilsTable.TABLE_NAME, null, null);
        try {
            JSONArray jsonArray = Talukaslist;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCC = jsonArray.getJSONObject(i);

                TehsilsContract Vc = new TehsilsContract();
                Vc.Sync(jsonObjectCC);

                ContentValues values = new ContentValues();

                values.put(TehsilsTable.COLUMN_TALUKA_CODE, Vc.getTalukacode());
                values.put(TehsilsTable.COLUMN_TALUKA_NAME, Vc.getTalukaName());

                db.insert(TehsilsTable.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    public void syncUCs(JSONArray UCslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UCsTable.TABLE_NAME, null, null);
        try {
            JSONArray jsonArray = UCslist;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCC = jsonArray.getJSONObject(i);

                UCsContract Vc = new UCsContract();
                Vc.Sync(jsonObjectCC);

                ContentValues values = new ContentValues();

                values.put(UCsTable.COLUMN_UCCODE, Vc.getUccode());
                values.put(UCsTable.COLUMN_UCS_NAME, Vc.getUcsName());
                values.put(UCsTable.COLUMN_TALUKA_CODE, Vc.getTaluka_code());

                db.insert(UCsTable.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    public void syncSchools(JSONArray Schoolslist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(singleSchool.TABLE_NAME, null, null);
        try {
            JSONArray jsonArray = Schoolslist;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCC = jsonArray.getJSONObject(i);

                SchoolContract Vc = new SchoolContract();
                Vc.sync(jsonObjectCC);

                ContentValues values = new ContentValues();

                values.put(singleSchool.COLUMN_DATE, Vc.getdt());
                values.put(singleSchool.COLUMN_DEVICE_ID, Vc.getDeviceid());
                values.put(singleSchool.COLUMN_SERIAL_NO, Vc.getSerialno());

                db.insert(singleSchool.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    public Collection<TehsilsContract> getAllTalukas() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                TehsilsTable._ID,
                TehsilsTable.COLUMN_TALUKA_CODE,
                TehsilsTable.COLUMN_TALUKA_NAME
        };

        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                TehsilsTable.COLUMN_TALUKA_NAME + " ASC";

        Collection<TehsilsContract> allDC = new ArrayList<>();
        try {
            c = db.query(
                    TehsilsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                TehsilsContract dc = new TehsilsContract();
                allDC.add(dc.HydrateTalukas(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allDC;
    }

    public SchoolContract getSerialWRTDate(String date) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                singleSchool._ID,
                singleSchool.COLUMN_DEVICE_ID,
                singleSchool.COLUMN_DATE,
                singleSchool.COLUMN_SERIAL_NO,
        };

        String whereClause = singleSchool.COLUMN_DATE + " =?";
        String[] whereArgs = new String[]{date};
        String groupBy = null;
        String having = null;

        String orderBy =
                singleSchool._ID + " ASC";

        SchoolContract allDC = new SchoolContract();
        try {
            c = db.query(
                    singleSchool.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                allDC.hydrate(c);
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allDC;
    }

    public Collection<UCsContract> getAllUCsByTalukas(String taluka_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                UCsTable._ID,
                UCsTable.COLUMN_UCCODE,
                UCsTable.COLUMN_UCS_NAME,
                UCsTable.COLUMN_TALUKA_CODE
        };

        String whereClause = UCsTable.COLUMN_TALUKA_CODE + " = ?";
        String[] whereArgs = {taluka_code};
        String groupBy = null;
        String having = null;

        String orderBy =
                UCsTable.COLUMN_UCS_NAME + " ASC";

        Collection<UCsContract> allPC = new ArrayList<>();
        try {
            c = db.query(
                    UCsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                UCsContract pc = new UCsContract();
                allPC.add(pc.hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allPC;
    }

    public void syncUser(JSONArray userlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UsersTable.TABLE_NAME, null, null);
        try {
            JSONArray jsonArray = userlist;
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObjectUser = jsonArray.getJSONObject(i);

                UsersContract user = new UsersContract();
                user.Sync(jsonObjectUser);
                ContentValues values = new ContentValues();

                values.put(UsersContract.UsersTable.ROW_USERNAME, user.getUserName());
                values.put(UsersTable.ROW_PASSWORD, user.getPassword());
                values.put(UsersTable.ROW_TEAM, user.getROW_TEAM());
                db.insert(UsersTable.TABLE_NAME, null, values);
            }


        } catch (Exception e) {
            Log.d(TAG, "syncUser(e): " + e);
        } finally {
            db.close();
        }
    }

    public boolean Login(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {

//      New value for one column
            String[] columns = {
                    UsersTable._ID,
                    UsersTable.ROW_USERNAME,
                    UsersTable.ROW_TEAM
            };

// Which row to update, based on the ID
            String selection = UsersContract.UsersTable.ROW_USERNAME + " = ?" + " AND " + UsersContract.UsersTable.ROW_PASSWORD + " = ?";
            String[] selectionArgs = {username, password};
            cursor = db.query(UsersContract.UsersTable.TABLE_NAME, //Table to query
                    columns,                    //columns to return
                    selection,                  //columns for the WHERE clause
                    selectionArgs,              //The values for the WHERE clause
                    null,                       //group the rows
                    null,                       //filter by row groups
                    null);                      //The sort order

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    MainApp.teamNo = cursor.getString(cursor.getColumnIndex(UsersTable.ROW_TEAM));
                }
                return true;
            }
        } catch (Exception e) {

        } finally {
            cursor.close();
            db.close();
        }
        return false;
    }

    public List<FormsContract> getFormsByDSS(String dssID) {
        List<FormsContract> formList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FormsTable.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                FormsContract fc = new FormsContract();
                formList.add(fc.Hydrate(c));
            } while (c.moveToNext());
        }

        // return contact list
        return formList;
    }

    public Long addForm(FormsContract fc) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_PROJECT_NAME, fc.getProjectName());
        values.put(FormsTable.COLUMN_UID, fc.getUID());
        values.put(FormsTable.COLUMN_FORMDATE, fc.getFormDate());
        values.put(FormsTable.COLUMN_USER, fc.getUser());
        //values.put(FormsChildTable.COLUMN_FORMTYPE, fc.getFormtype());
        values.put(FormsTable.COLUMN_ISTATUS, fc.getIstatus());
        values.put(FormsTable.COLUMN_ISTATUS88x, fc.getIstatus88x());
        values.put(FormsTable.COLUMN_SA, fc.getsA());
        values.put(FormsTable.COLUMN_SB, fc.getsB());
        values.put(FormsTable.COLUMN_SC, fc.getsC());
        values.put(FormsTable.COLUMN_SD, fc.getsD());
        values.put(FormsTable.COLUMN_CHILD, fc.getsD());
        values.put(FormsTable.COLUMN_GPSLAT, fc.getGpsLat());
        values.put(FormsTable.COLUMN_GPSLNG, fc.getGpsLng());
        values.put(FormsTable.COLUMN_GPSDATE, fc.getGpsDT());
        values.put(FormsTable.COLUMN_GPSACC, fc.getGpsAcc());
        values.put(FormsTable.COLUMN_DEVICETAGID, fc.getDevicetagID());
        values.put(FormsTable.COLUMN_DEVICEID, fc.getDeviceID());
        values.put(FormsTable.COLUMN_SYNCED, fc.getSynced());
        values.put(FormsTable.COLUMN_SYNCED_DATE, fc.getSynced_date());
        values.put(FormsTable.COLUMN_APP_VERSION, fc.getAppversion());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FormsTable.TABLE_NAME,
                FormsTable.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }


    public Long addChildForm(ChildContract fc) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FormsChildTable.COLUMN_PROJECT_NAME, fc.getProjectName());
        values.put(FormsChildTable.COLUMN_UID, fc.getUID());
        values.put(FormsChildTable.COLUMN_UUID, fc.getUUID());
        values.put(FormsChildTable.COLUMN_FORMDATE, fc.getFormDate());
        values.put(FormsChildTable.COLUMN_USER, fc.getUser());
        values.put(FormsChildTable.COLUMN_ISTATUS, fc.getIstatus());
        values.put(FormsChildTable.COLUMN_SB, fc.getsB());
        values.put(FormsChildTable.COLUMN_GPSLAT, fc.getGpsLat());
        values.put(FormsChildTable.COLUMN_GPSLNG, fc.getGpsLng());
        values.put(FormsChildTable.COLUMN_GPSDATE, fc.getGpsDT());
        values.put(FormsChildTable.COLUMN_GPSACC, fc.getGpsAcc());
        values.put(FormsChildTable.COLUMN_DEVICETAGID, fc.getDevicetagID());
        values.put(FormsChildTable.COLUMN_DEVICEID, fc.getDeviceID());
        values.put(FormsChildTable.COLUMN_SYNCED, fc.getSynced());
        values.put(FormsChildTable.COLUMN_SYNCED_DATE, fc.getSynced_date());
        values.put(FormsChildTable.COLUMN_APP_VERSION, fc.getAppversion());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FormsChildTable.TABLE_NAME,
                FormsChildTable.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }


    public Long addEnrollmentForm(EnrollmentContract ec) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(EnrollChildTable.COLUMN_PROJECT_NAME, ec.getProjectName());
        values.put(EnrollChildTable.COLUMN_UID, ec.getUID());
        values.put(EnrollChildTable.COLUMN_UUID, ec.getUUID());
        values.put(EnrollChildTable.COLUMN_FORMDATE, ec.getFormDate());
        values.put(EnrollChildTable.COLUMN_USER, ec.getUser());
        values.put(EnrollChildTable.COLUMN_ISTATUS, ec.getIstatus());
        values.put(EnrollChildTable.COLUMN_SC, ec.getsC());
        values.put(EnrollChildTable.COLUMN_GPSLAT, ec.getGpsLat());
        values.put(EnrollChildTable.COLUMN_GPSLNG, ec.getGpsLng());
        values.put(EnrollChildTable.COLUMN_GPSDATE, ec.getGpsDT());
        values.put(EnrollChildTable.COLUMN_GPSACC, ec.getGpsAcc());
        values.put(EnrollChildTable.COLUMN_DEVICETAGID, ec.getDevicetagID());
        values.put(EnrollChildTable.COLUMN_DEVICEID, ec.getDeviceID());
        values.put(EnrollChildTable.COLUMN_SYNCED, ec.getSynced());
        values.put(EnrollChildTable.COLUMN_SYNCED_DATE, ec.getSynced_date());
        values.put(EnrollChildTable.COLUMN_APP_VERSION, ec.getAppversion());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                EnrollChildTable.TABLE_NAME,
                EnrollChildTable.COLUMN_NAME_NULLABLE,
                values);

        return newRowId;
    }


    public Long addSerialForm(SchoolContract sc) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(singleSchool.COLUMN_DEVICE_ID, sc.getDeviceid());
        values.put(singleSchool.COLUMN_DATE, sc.getdt());
        values.put(singleSchool.COLUMN_SERIAL_NO, sc.getSerialno());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                singleSchool.TABLE_NAME,
                singleSchool.COLUMN_NAME_NULLABLE,
                values);
        return newRowId;
    }

    public int updateSerialWRTDate(String date, String serial) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(singleSchool.COLUMN_SERIAL_NO, serial);

        // Which row to update, based on the title
        String where = singleSchool.COLUMN_DATE + " = ?";
        String[] whereArgs = {date};

        int count = db.update(
                singleSchool.TABLE_NAME,
                values,
                where,
                whereArgs);

        return count;
    }


    public void updateSyncedForms(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_SYNCED, true);
        values.put(FormsTable.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = FormsTable._ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                FormsTable.TABLE_NAME,
                values,
                where,
                whereArgs);
    }


    public void updateSyncedChildForm(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsChildTable.COLUMN_SYNCED, true);
        values.put(FormsChildTable.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = FormsChildTable._ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                FormsChildTable.TABLE_NAME,
                values,
                where,
                whereArgs);
    }


    public void updateSyncedEnrollmentForm(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(EnrollChildTable.COLUMN_SYNCED, true);
        values.put(EnrollChildTable.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = EnrollChildTable._ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                EnrollChildTable.TABLE_NAME,
                values,
                where,
                whereArgs);
    }


    public void updateSyncedSerial(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(singleSchool.COLUMN_SYNCED, true);
        values.put(singleSchool.COLUMN_SYNCED_DATE, new Date().toString());

// Which row to update, based on the title
        String where = singleSchool._ID + " = ?";
        String[] whereArgs = {id};

        int count = db.update(
                singleSchool.TABLE_NAME,
                values,
                where,
                whereArgs);
    }


    public int updateFormID() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_UID, MainApp.fc.getUID());

// Which row to update, based on the ID
        String selection = FormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateFormChildID() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsChildTable.COLUMN_UID, MainApp.cc.getUID());

// Which row to update, based on the ID
        String selection = FormsChildTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.cc.get_ID())};

        int count = db.update(FormsChildTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateFormEnrollmentID() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(EnrollChildTable.COLUMN_UID, MainApp.ec.getUID());

// Which row to update, based on the ID
        String selection = EnrollChildTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.ec.get_ID())};

        int count = db.update(EnrollChildTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public Collection<FormsContract> getUnsyncedForms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsTable._ID,
                FormsTable.COLUMN_UID,
                FormsTable.COLUMN_FORMDATE,
                FormsTable.COLUMN_USER,
                FormsTable.COLUMN_ISTATUS,
                //FormsTable.COLUMN_FORMTYPE,
                FormsTable.COLUMN_SA,
                FormsTable.COLUMN_SB,
                FormsTable.COLUMN_SC,
                FormsTable.COLUMN_SD,
                FormsTable.COLUMN_GPSLAT,
                FormsTable.COLUMN_GPSLNG,
                FormsTable.COLUMN_GPSDATE,
                FormsTable.COLUMN_GPSACC,
                FormsTable.COLUMN_DEVICETAGID,
                FormsTable.COLUMN_DEVICEID,
                FormsTable.COLUMN_SYNCED,
                FormsTable.COLUMN_SYNCED_DATE,
                FormsTable.COLUMN_APP_VERSION
        };
        String whereClause = FormsTable.COLUMN_SYNCED + " is null OR " + FormsTable.COLUMN_SYNCED + " = '' ";
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsTable._ID + " ASC";

        Collection<FormsContract> allFC = new ArrayList<FormsContract>();
        try {
            c = db.query(
                    FormsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsContract fc = new FormsContract();
                allFC.add(fc.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFC;
    }


    public Collection<ChildContract> getUnsyncedChildForms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsChildTable._ID,
                FormsChildTable.COLUMN_UID,
                FormsChildTable.COLUMN_UUID,
                FormsChildTable.COLUMN_FORMDATE,
                FormsChildTable.COLUMN_USER,
                FormsChildTable.COLUMN_ISTATUS,
                FormsChildTable.COLUMN_SB,
                FormsChildTable.COLUMN_GPSLAT,
                FormsChildTable.COLUMN_GPSLNG,
                FormsChildTable.COLUMN_GPSDATE,
                FormsChildTable.COLUMN_GPSACC,
                FormsChildTable.COLUMN_DEVICETAGID,
                FormsChildTable.COLUMN_DEVICEID,
                FormsChildTable.COLUMN_SYNCED,
                FormsChildTable.COLUMN_SYNCED_DATE,
                FormsChildTable.COLUMN_APP_VERSION
        };
        String whereClause = FormsChildTable.COLUMN_SYNCED + " is null OR " + FormsChildTable.COLUMN_SYNCED + " = '' ";
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsChildTable._ID + " ASC";

        Collection<ChildContract> allFC = new ArrayList<ChildContract>();
        try {
            c = db.query(
                    FormsChildTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                ChildContract fc = new ChildContract();
                allFC.add(fc.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFC;
    }


    public Collection<EnrollmentContract> getUnsyncedEnrollmentForms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                EnrollChildTable._ID,
                EnrollChildTable.COLUMN_UID,
                EnrollChildTable.COLUMN_UUID,
                EnrollChildTable.COLUMN_FORMDATE,
                EnrollChildTable.COLUMN_USER,
                EnrollChildTable.COLUMN_ISTATUS,
                EnrollChildTable.COLUMN_SC,
                EnrollChildTable.COLUMN_GPSLAT,
                EnrollChildTable.COLUMN_GPSLNG,
                EnrollChildTable.COLUMN_GPSDATE,
                EnrollChildTable.COLUMN_GPSACC,
                EnrollChildTable.COLUMN_DEVICETAGID,
                EnrollChildTable.COLUMN_DEVICEID,
                EnrollChildTable.COLUMN_SYNCED,
                EnrollChildTable.COLUMN_SYNCED_DATE,
                EnrollChildTable.COLUMN_APP_VERSION
        };
        String whereClause = EnrollChildTable.COLUMN_SYNCED + " is null OR " + EnrollChildTable.COLUMN_SYNCED + " = '' ";
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy = EnrollChildTable._ID + " ASC";

        Collection<EnrollmentContract> allFC = new ArrayList<EnrollmentContract>();
        try {
            c = db.query(
                    EnrollChildTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                EnrollmentContract fc = new EnrollmentContract();
                allFC.add(fc.Hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFC;
    }


    public Collection<SchoolContract> getUnsyncedSerials() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                singleSchool._ID,
                singleSchool.COLUMN_DEVICE_ID,
                singleSchool.COLUMN_DATE,
                singleSchool.COLUMN_SERIAL_NO,
                singleSchool.COLUMN_SYNCED,
                singleSchool.COLUMN_SYNCED_DATE
        };

        String whereClause = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;

        String orderBy =
                singleSchool._ID + " ASC";

        Collection<SchoolContract> allFC = new ArrayList<SchoolContract>();
        try {
            c = db.query(
                    singleSchool.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                SchoolContract fc = new SchoolContract();
                allFC.add(fc.hydrate(c));
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFC;
    }


    public Collection<FormsContract> getTodayForms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        String[] columns = {
                FormsTable._ID,
                //FormsChildTable.COLUMN_DSSID,
                FormsTable.COLUMN_FORMDATE,
                FormsTable.COLUMN_ISTATUS,
                FormsTable.COLUMN_SYNCED,

        };
        String whereClause = FormsTable.COLUMN_FORMDATE + " Like ? ";
        String[] whereArgs = new String[]{"%" + spDateT.substring(0, 8).trim() + "%"};
        String groupBy = null;
        String having = null;

        String orderBy =
                FormsTable._ID + " ASC";

        Collection<FormsContract> allFC = new ArrayList<>();
        try {
            c = db.query(
                    FormsTable.TABLE_NAME,  // The table to query
                    columns,                   // The columns to return
                    whereClause,               // The columns for the WHERE clause
                    whereArgs,                 // The values for the WHERE clause
                    groupBy,                   // don't group the rows
                    having,                    // don't filter by row groups
                    orderBy                    // The sort order
            );
            while (c.moveToNext()) {
                FormsContract fc = new FormsContract();
                fc.set_ID(c.getString(c.getColumnIndex(FormsTable._ID)));
                fc.setFormDate(c.getString(c.getColumnIndex(FormsTable.COLUMN_FORMDATE)));
                fc.setIstatus(c.getString(c.getColumnIndex(FormsTable.COLUMN_ISTATUS)));
                fc.setSynced(c.getString(c.getColumnIndex(FormsTable.COLUMN_SYNCED)));
                allFC.add(fc);
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return allFC;
    }

    // ANDROID DATABASE MANAGER
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }

    public int updateSA() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_SA, MainApp.fc.getsA());

// Which row to update, based on the ID
        String selection = FormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateSB() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_SB, MainApp.fc.getsB());

// Which row to update, based on the ID
        String selection = FormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateSC() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_SC, MainApp.fc.getsC());

// Which row to update, based on the ID
        String selection = FormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateSD() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_SD, MainApp.fc.getsD());

// Which row to update, based on the ID
        String selection = FormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }


    public int updateCount() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_SB, MainApp.fc.getsB());

// Which row to update, based on the ID
        String selection = FormsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }


    public int updateEnding() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FormsTable.COLUMN_ISTATUS, MainApp.fc.getIstatus());
        values.put(FormsTable.COLUMN_ISTATUS88x, MainApp.fc.getIstatus88x());

// Which row to update, based on the ID
        String selection = FormsTable._ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.fc.get_ID())};

        int count = db.update(FormsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int updateEnrollmentEnding() {
        SQLiteDatabase db = this.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(EnrollChildTable.COLUMN_ISTATUS, MainApp.ec.getIstatus());

// Which row to update, based on the ID
        String selection = EnrollChildTable._ID + " =? ";
        String[] selectionArgs = {String.valueOf(MainApp.ec.get_ID())};

        int count = db.update(EnrollChildTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count;
    }

}