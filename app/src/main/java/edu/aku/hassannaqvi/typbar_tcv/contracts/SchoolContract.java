package edu.aku.hassannaqvi.typbar_tcv.contracts;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javed.khan on 1/22/2018.
 */

public class SchoolContract {

    private String id;
    private String deviceid;
    private String dt;
    private String serialno;
    private String synced;
    private String synced_date;


    public SchoolContract() {
    }

    public SchoolContract(String deviceid, String dt, String serialno) {
        this.deviceid = deviceid;
        this.dt = dt;
        this.serialno = serialno;
    }

    public SchoolContract sync(JSONObject jsonObject) throws JSONException {
        this.deviceid = jsonObject.getString(singleSchool.COLUMN_DEVICE_ID);
        this.dt = jsonObject.getString(singleSchool.COLUMN_DATE);
        this.serialno = jsonObject.getString(singleSchool.COLUMN_SERIAL_NO);
        this.synced = jsonObject.getString(singleSchool.COLUMN_SYNCED);
        this.synced_date = jsonObject.getString(singleSchool.COLUMN_SYNCED_DATE);

        return this;
    }

    public SchoolContract hydrate(Cursor cursor) {
        this.deviceid = cursor.getString(cursor.getColumnIndex(singleSchool.COLUMN_DEVICE_ID));
        this.dt = cursor.getString(cursor.getColumnIndex(singleSchool.COLUMN_DATE));
        this.serialno = cursor.getString(cursor.getColumnIndex(singleSchool.COLUMN_SERIAL_NO));
        return this;
    }


    public JSONObject toJSONObject() throws JSONException {

        JSONObject json = new JSONObject();

        json.put(singleSchool._ID, this.id == null ? JSONObject.NULL : this.id);
        json.put(singleSchool.COLUMN_DEVICE_ID, this.deviceid == null ? JSONObject.NULL : this.deviceid);
        json.put(singleSchool.COLUMN_DATE, this.dt == null ? JSONObject.NULL : this.dt);
        json.put(singleSchool.COLUMN_SERIAL_NO, this.serialno == null ? JSONObject.NULL : this.serialno);
        json.put(singleSchool.COLUMN_SYNCED, this.synced == null ? JSONObject.NULL : this.synced);
        json.put(singleSchool.COLUMN_SYNCED_DATE, this.synced_date == null ? JSONObject.NULL : this.synced_date);

        return json;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getdt() {
        return dt;
    }

    public String getSerialno() {
        return serialno;
    }


    public static abstract class singleSchool implements BaseColumns {

        public static final String TABLE_NAME = "schools";
        public static final String COLUMN_NAME_NULLABLE = "nullColumnHack";
        public static final String _ID = "_id";
        public static final String COLUMN_DEVICE_ID = "deviceid";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_SERIAL_NO = "serialno";
        public static final String COLUMN_SYNCED = "synced";
        public static final String COLUMN_SYNCED_DATE = "synced_date";

        public static final String _URI = "schools.php";

    }

}