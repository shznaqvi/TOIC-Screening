package edu.aku.hassannaqvi.toic_screening.contracts;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javed.khan on 1/22/2018.
 */

public class SerialContract {

    private String id;
    private String deviceid;
    private String dt;
    private String serialno;
    private String synced;
    private String synced_date;


    public SerialContract() {
    }

    public SerialContract(String deviceid, String dt, String serialno) {
        this.deviceid = deviceid;
        this.dt = dt;
        this.serialno = serialno;
    }

    public SerialContract sync(JSONObject jsonObject) throws JSONException {
        this.deviceid = jsonObject.getString(singleSerial.COLUMN_DEVICE_ID);
        this.dt = jsonObject.getString(singleSerial.COLUMN_DATE);
        this.serialno = jsonObject.getString(singleSerial.COLUMN_SERIAL_NO);
        this.synced = jsonObject.getString(singleSerial.COLUMN_SYNCED);
        this.synced_date = jsonObject.getString(singleSerial.COLUMN_SYNCED_DATE);

        return this;
    }

    public SerialContract hydrate(Cursor cursor) {
        this.deviceid = cursor.getString(cursor.getColumnIndex(singleSerial.COLUMN_DEVICE_ID));
        this.dt = cursor.getString(cursor.getColumnIndex(singleSerial.COLUMN_DATE));
        this.serialno = cursor.getString(cursor.getColumnIndex(singleSerial.COLUMN_SERIAL_NO));
        return this;
    }


    public JSONObject toJSONObject() throws JSONException {

        JSONObject json = new JSONObject();

        json.put(singleSerial._ID, this.id == null ? JSONObject.NULL : this.id);
        json.put(singleSerial.COLUMN_DEVICE_ID, this.deviceid == null ? JSONObject.NULL : this.deviceid);
        json.put(singleSerial.COLUMN_DATE, this.dt == null ? JSONObject.NULL : this.dt);
        json.put(singleSerial.COLUMN_SERIAL_NO, this.serialno == null ? JSONObject.NULL : this.serialno);
        json.put(singleSerial.COLUMN_SYNCED, this.synced == null ? JSONObject.NULL : this.synced);
        json.put(singleSerial.COLUMN_SYNCED_DATE, this.synced_date == null ? JSONObject.NULL : this.synced_date);

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


    public static abstract class singleSerial implements BaseColumns {

        public static final String TABLE_NAME = "serials";
        public static final String COLUMN_NAME_NULLABLE = "nullColumnHack";
        public static final String _ID = "_id";
        public static final String COLUMN_DEVICE_ID = "deviceid";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_SERIAL_NO = "serialno";
        public static final String COLUMN_SYNCED = "synced";
        public static final String COLUMN_SYNCED_DATE = "synced_date";

        public static final String _URI = "serials.php";

    }

}