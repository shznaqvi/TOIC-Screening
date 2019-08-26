package edu.aku.hassannaqvi.typbar_tcv.contracts;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class CCChildrenContract {

    private String luid;
    private String lformdate;
    private String tcvscaa01;
    private String tcvscaa05;
    private String tcvscaa05y;
    private String tcvscaa05m;
    private String tcvscaa07;
    private String tcvscaa08;
    private String tcvscab23;

    public CCChildrenContract(String tcvscaa05, String tcvscaa05y, String tcvscaa05m) {
        this.tcvscaa05 = tcvscaa05;
        this.tcvscaa05y = tcvscaa05y;
        this.tcvscaa05m = tcvscaa05m;
    }

    public CCChildrenContract() {
    }

    public String getLuid() {
        return luid;
    }

    public void setLuid(String luid) {
        this.luid = luid;
    }

    public String getLformdate() {
        return lformdate;
    }

    public void setLformdate(String lformdate) {
        this.lformdate = lformdate;
    }

    public String getTcvscaa01() {
        return tcvscaa01;
    }

    public void setTcvscaa01(String tcvscaa01) {
        this.tcvscaa01 = tcvscaa01;
    }

    public String getTcvscaa05() {
        return tcvscaa05;
    }

    public void setTcvscaa05(String tcvscaa05) {
        this.tcvscaa05 = tcvscaa05;
    }

    public String getTcvscaa05y() {
        return tcvscaa05y;
    }

    public void setTcvscaa05y(String tcvscaa05y) {
        this.tcvscaa05y = tcvscaa05y;
    }

    public String getTcvscaa05m() {
        return tcvscaa05m;
    }

    public void setTcvscaa05m(String tcvscaa05m) {
        this.tcvscaa05m = tcvscaa05m;
    }

    public String getTcvscaa07() {
        return tcvscaa07;
    }

    public void setTcvscaa07(String tcvscaa07) {
        this.tcvscaa07 = tcvscaa07;
    }

    public String getTcvscaa08() {
        return tcvscaa08;
    }

    public void setTcvscaa08(String tcvscaa08) {
        this.tcvscaa08 = tcvscaa08;
    }

    public String getTcvscab23() {
        return tcvscab23;
    }

    public void setTcvscab23(String tcvscab23) {
        this.tcvscab23 = tcvscab23;
    }

    public CCChildrenContract sync(JSONObject jsonObject) throws JSONException {
        this.luid = jsonObject.getString(ChildrenEntry.COLUMN_LUID);
        this.lformdate = jsonObject.getString(ChildrenEntry.COLUMN_LFORMDATE);
        this.tcvscaa01 = jsonObject.getString(ChildrenEntry.COLUMN_TCVSCAA01);
        this.tcvscaa05 = jsonObject.getString(ChildrenEntry.COLUMN_TCVSCAA05);
        this.tcvscaa05y = jsonObject.getString(ChildrenEntry.COLUMN_TCVSCAA05Y);
        this.tcvscaa05m = jsonObject.getString(ChildrenEntry.COLUMN_TCVSCAA05M);
        this.tcvscaa07 = jsonObject.getString(ChildrenEntry.COLUMN_TCVSCAA07);
        this.tcvscaa08 = jsonObject.getString(ChildrenEntry.COLUMN_TCVSCAA08);
        this.tcvscab23 = jsonObject.getString(ChildrenEntry.COLUMN_TCVSCAB23);

        return this;
    }

    public CCChildrenContract hydrate(Cursor cursor) {
        this.luid = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_LUID));
        this.lformdate = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_LFORMDATE));
        this.tcvscaa01 = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_TCVSCAA01));
        this.tcvscaa05 = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_TCVSCAA05));
        this.tcvscaa05y = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_TCVSCAA05Y));
        this.tcvscaa05m = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_TCVSCAA05M));
        this.tcvscaa07 = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_TCVSCAA07));
        this.tcvscaa08 = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_TCVSCAA08));
        this.tcvscab23 = cursor.getString(cursor.getColumnIndex(ChildrenEntry.COLUMN_TCVSCAB23));

        return this;
    }

    public CCChildrenContract hydrateDB(Cursor cursor) {
        FormContractChildren childrenDB = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(FormsContract.FormsTable.COLUMN_SA)), FormContractChildren.class);

        this.luid = cursor.getString(cursor.getColumnIndex(FormsContract.FormsTable.COLUMN_UID));
        this.lformdate = cursor.getString(cursor.getColumnIndex(FormsContract.FormsTable.COLUMN_FORMDATE));
        this.tcvscaa01 = childrenDB.getTcvscaa01();
        this.tcvscaa05 = childrenDB.getTcvscaa05();
        this.tcvscaa05y = childrenDB.getTcvscaa05y();
        this.tcvscaa05m = childrenDB.getTcvscaa05m();
        this.tcvscaa07 = childrenDB.getTcvscaa07();
        this.tcvscaa08 = childrenDB.getTcvscaa08();
        this.tcvscab23 = childrenDB.getTcvscab23();

        return this;
    }

    public CCChildrenContract hydrateDBControl(Cursor cursor) {
        FormContractChildrenControl childrenDB = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(FormsContract.FormsTable.COLUMN_SA)), FormContractChildrenControl.class);

        this.luid = cursor.getString(cursor.getColumnIndex(FormsContract.FormsTable.COLUMN_UID));
        this.lformdate = cursor.getString(cursor.getColumnIndex(FormsContract.FormsTable.COLUMN_FORMDATE));
        this.tcvscaa01 = childrenDB.getTcvscaa01();
        this.tcvscaa05 = childrenDB.getTcvscaa05();
        this.tcvscaa05y = childrenDB.getTcvscaa05y();
        this.tcvscaa05m = childrenDB.getTcvscaa05m();
        this.tcvscaa07 = childrenDB.getTcvscaa07();
        this.tcvscaa08 = childrenDB.getTcvscaa08();
        this.tcvscab23 = childrenDB.getTcvscab23();

        return this;
    }

    private class FormContractChildren {
        String tcvscaa01, tcvscaa05, tcvscaa05y, tcvscaa05m, tcvscaa07, tcvscaa08, tcvscab23;

        public String getTcvscaa01() {
            return tcvscaa01;
        }

        public String getTcvscaa05() {
            return tcvscaa05;
        }

        public String getTcvscaa05y() {
            return tcvscaa05y;
        }

        public String getTcvscaa05m() {
            return tcvscaa05m;
        }

        public String getTcvscaa07() {
            return tcvscaa07;
        }

        public String getTcvscaa08() {
            return tcvscaa08;
        }

        public String getTcvscab23() {
            return tcvscab23;
        }
    }

    private class FormContractChildrenControl {

        @SerializedName("tcvscla01")
        String tcvscaa01;
        @SerializedName("ch_screendt")
        String tcvscaa05;
        @SerializedName("tcvscla05")
        String tcvscaa05y;
        @SerializedName("tcvscla05m")
        String tcvscaa05m;
        @SerializedName("tcvscla07")
        String tcvscaa07;
        @SerializedName("tcvscla08")
        String tcvscaa08;
        @SerializedName("tcvscla19")
        String tcvscab23;

        public String getTcvscaa01() {
            return tcvscaa01;
        }

        public String getTcvscaa05() {
            return tcvscaa05;
        }

        public String getTcvscaa05y() {
            return tcvscaa05y;
        }

        public String getTcvscaa05m() {
            return tcvscaa05m;
        }

        public String getTcvscaa07() {
            return tcvscaa07;
        }

        public String getTcvscaa08() {
            return tcvscaa08;
        }

        public String getTcvscab23() {
            return tcvscab23;
        }
    }

    public final class ChildrenEntry implements BaseColumns {
        public static final String TABLE_NAME = "ccchild";

        public static final String COLUMN_LUID = "luid";
        public static final String COLUMN_LFORMDATE = "lformdate";
        public static final String COLUMN_TCVSCAA01 = "tcvscaa01";
        public static final String COLUMN_TCVSCAA05 = "tcvscaa05";
        public static final String COLUMN_TCVSCAA05Y = "tcvscaa05y";
        public static final String COLUMN_TCVSCAA05M = "tcvscaa05m";
        public static final String COLUMN_TCVSCAA07 = "tcvscaa07";
        public static final String COLUMN_TCVSCAA08 = "tcvscaa08";
        public static final String COLUMN_TCVSCAB23 = "tcvscab23";

        public static final String _URI = "children.php";
    }

}
