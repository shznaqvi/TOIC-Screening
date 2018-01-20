package edu.aku.hassannaqvi.toic_screening.contracts;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hassan.naqvi on 10/31/2016.
 */

public class VillagesContract {

    private String villageCode;
    private String villageName;
    private String districtCode;


    public VillagesContract() {
    }

    public VillagesContract sync(JSONObject jsonObject) throws JSONException {
        this.villageCode = jsonObject.getString(singleVillage.COLUMN_VILLAGE_CODE);
        this.villageName = jsonObject.getString(singleVillage.COLUMN_VILLAGE_NAME);
        this.districtCode = jsonObject.getString(singleVillage.COLUMN_DISTRICT_CODE);


        return this;
    }

    public VillagesContract hydrate(Cursor cursor) {
        this.villageCode = cursor.getString(cursor.getColumnIndex(singleVillage.COLUMN_VILLAGE_CODE));
        this.villageName = cursor.getString(cursor.getColumnIndex(singleVillage.COLUMN_VILLAGE_NAME));
        this.districtCode = cursor.getString(cursor.getColumnIndex(singleVillage.COLUMN_DISTRICT_CODE));

        return this;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public static abstract class singleVillage implements BaseColumns
    {

        public static final String TABLE_NAME = "villages";
        public static final String COLUMN_NAME_NULLABLE = "nullColumnHack";
        public static final String _ID = "_ID";
        public static final String COLUMN_VILLAGE_CODE = "village_code";
        public static final String COLUMN_VILLAGE_NAME = "village_name";
        public static final String COLUMN_DISTRICT_CODE = "district_code";

        public static final String _URI = "villages.php";

    }

}
