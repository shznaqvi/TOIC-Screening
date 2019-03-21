package edu.aku.hassannaqvi.typbar_tcv.contracts;


import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

public class TehsilsContract {

    private static final String TAG = "Talukas_CONTRACT";
    String talukacode;
    String taluka;

    public TehsilsContract() {
        // Default Constructor
    }

    public TehsilsContract Sync(JSONObject jsonObject) throws JSONException {
        this.talukacode = jsonObject.getString(TehsilsTable.COLUMN_TALUKA_CODE);
        this.taluka = jsonObject.getString(TehsilsTable.COLUMN_TALUKA_NAME);
        return this;
    }

    public TehsilsContract HydrateTalukas(Cursor cursor) {
        this.talukacode = cursor.getString(cursor.getColumnIndex(TehsilsTable.COLUMN_TALUKA_CODE));
        this.taluka = cursor.getString(cursor.getColumnIndex(TehsilsTable.COLUMN_TALUKA_NAME));
        return this;
    }

    public String getTalukacode() {
        return talukacode;
    }

    public void setTalukacode(String talukacode) {
        this.talukacode = talukacode;
    }

    public String getTalukaName() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public JSONObject toJSONObject() throws JSONException {

        JSONObject json = new JSONObject();
        json.put(TehsilsTable.COLUMN_TALUKA_CODE, this.talukacode == null ? JSONObject.NULL : this.talukacode);
        json.put(TehsilsTable.COLUMN_TALUKA_NAME, this.taluka == null ? JSONObject.NULL : this.taluka);
        return json;
    }


    public static abstract class TehsilsTable implements BaseColumns {

        public static final String TABLE_NAME = "tehsils";
        public static final String COLUMN_TALUKA_CODE = "town_code";
        public static final String COLUMN_TALUKA_NAME = "town_name";

        public static final String _URI = "towns.php";
    }
}