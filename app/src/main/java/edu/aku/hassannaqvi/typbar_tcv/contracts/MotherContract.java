package edu.aku.hassannaqvi.typbar_tcv.contracts;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hassan.naqvi on 11/30/2016.
 */

public class MotherContract {

    private final String projectName = "TYBAR_TCV";
    //private final String surveyType = "SN";
    private String _ID = "";
    private String _UID = "";
    private String formDate = ""; // Date
    private String user = ""; // Interviewer
    private String FMUID = "";
    private String UUID = ""; // Interview Status
    private String sC = ""; //
    private String sD = "";
    private String deviceID = "";
    private String devicetagID = "";
    private String appversion;
    private String synced = "";
    private String synced_date = "";

    public MotherContract() {
    }

    public MotherContract Sync(JSONObject jsonObject) throws JSONException {
        this._ID = jsonObject.getString(MothersTable._ID);
        this._UID = jsonObject.getString(MothersTable.COLUMN_UID);
        this.formDate = jsonObject.getString(MothersTable.COLUMN_FORMDATE);
        this.user = jsonObject.getString(MothersTable.COLUMN_USER);
        this.UUID = jsonObject.getString(MothersTable.COLUMN_UUID);
        this.FMUID = jsonObject.getString(MothersTable.COLUMN_FMUID);
        this.sC = jsonObject.getString(MothersTable.COLUMN_SC);
        this.sD = jsonObject.getString(MothersTable.COLUMN_SD);
        this.deviceID = jsonObject.getString(MothersTable.COLUMN_DEVICEID);
        this.devicetagID = jsonObject.getString(MothersTable.COLUMN_DEVICETAGID);
        this.appversion = jsonObject.getString(MothersTable.COLUMN_APP_VERSION);

        return this;

    }

    public MotherContract Hydrate(Cursor cursor) {
        this._ID = cursor.getString(cursor.getColumnIndex(MothersTable._ID));
        this._UID = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_UID));
        this.formDate = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_FORMDATE));
        this.user = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_USER));
        this.UUID = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_UUID));
        this.FMUID = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_FMUID));
        this.sC = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_SC));
        this.sD = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_SD));
        this.deviceID = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_DEVICEID));
        this.devicetagID = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_DEVICETAGID));
        this.appversion = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_APP_VERSION));
        this.synced = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_SYNCED));
        this.synced_date = cursor.getString(cursor.getColumnIndex(MothersTable.COLUMN_SYNCED_DATE));

        // TODO:
        return this;
    }

    public JSONObject toJSONObject() throws JSONException {

        JSONObject json = new JSONObject();

        json.put(MothersTable.COLUMN_PROJECT_NAME, this.projectName);
        json.put(MothersTable._ID, this._ID == null ? JSONObject.NULL : this._ID);
        json.put(MothersTable.COLUMN_UID, this._UID == null ? JSONObject.NULL : this._UID);
        json.put(MothersTable.COLUMN_FORMDATE, this.formDate == null ? JSONObject.NULL : this.formDate);
        json.put(MothersTable.COLUMN_USER, this.user == null ? JSONObject.NULL : this.user);
        json.put(MothersTable.COLUMN_UUID, this.UUID == null ? JSONObject.NULL : this.UUID);
        json.put(MothersTable.COLUMN_FMUID, this.FMUID == null ? JSONObject.NULL : this.FMUID);

        if (!this.sC.equals("")) {

            json.put(MothersTable.COLUMN_SC, this.sC.equals("") ? JSONObject.NULL : new JSONObject(this.sC));
        }
        if (!this.sD.equals("")) {

            json.put(MothersTable.COLUMN_SD, this.sD.equals("") ? JSONObject.NULL : new JSONObject(this.sD));
        }
        json.put(MothersTable.COLUMN_DEVICEID, this.deviceID == null ? JSONObject.NULL : this.deviceID);
        json.put(MothersTable.COLUMN_DEVICETAGID, this.devicetagID == null ? JSONObject.NULL : this.devicetagID);
        json.put(MothersTable.COLUMN_APP_VERSION, this.appversion == null ? JSONObject.NULL : this.appversion);
        json.put(MothersTable.COLUMN_SYNCED, this.synced == null ? JSONObject.NULL : this.synced);
        json.put(MothersTable.COLUMN_SYNCED_DATE, this.synced_date == null ? JSONObject.NULL : this.synced_date);

        return json;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String get_UID() {
        return _UID;
    }

    public void set_UID(String _UID) {
        this._UID = _UID;
    }

    public String getFormDate() {
        return formDate;
    }

    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFMUID() {
        return FMUID;
    }

    public void setFMUID(String FMUID) {
        this.FMUID = FMUID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getsC() {
        return sC;
    }

    public void setsC(String sC) {
        this.sC = sC;
    }

    public String getsD() {
        return sD;
    }

    public void setsD(String sD) {
        this.sD = sD;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDevicetagID() {
        return devicetagID;
    }

    public void setDevicetagID(String devicetagID) {
        this.devicetagID = devicetagID;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getProjectName() {
        return projectName;
    }

    public static abstract class MothersTable implements BaseColumns {

        public static final String TABLE_NAME = "mothers_forms";
        public static final String COLUMN_NAME_NULLABLE = "NULLHACK";
        public static final String COLUMN_PROJECT_NAME = "projectname";
        public static final String _ID = "_id";
        public static final String COLUMN_UID = "_uid";
        public static final String COLUMN_FORMDATE = "formdate";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_UUID = "UUID";
        public static final String COLUMN_FMUID = "FMUID";
        public static final String COLUMN_SC = "sc";
        public static final String COLUMN_SD = "sd";
        public static final String COLUMN_DEVICEID = "deviceid";
        public static final String COLUMN_DEVICETAGID = "tagid";
        public static final String COLUMN_APP_VERSION = "appversion";
        public static final String COLUMN_SYNCED = "synced";
        public static final String COLUMN_SYNCED_DATE = "synced_date";

        public static String _URL1 = "mothers_forms.php";
    }
}
