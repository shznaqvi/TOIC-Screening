package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySectionChildBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class SectionChild extends Activity {

    ActivitySectionChildBinding bi;
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_child);
        bi.setCallback(this);

        setContentUI();
    }

    private void setContentUI() {
        this.setTitle(R.string.sec_clisting);
        deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    public void BtnContinue() {
        try {

            if (!formValidation()) return;
            SaveDraft();

            if (UpdateDB()) MainApp.endActivity(this, this, true);
            Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

        DatabaseHelper db = new DatabaseHelper(this);
        long updcount = db.addForm(MainApp.fc);
        MainApp.fc.set_ID(String.valueOf(updcount));
        if (updcount > 0) {
            MainApp.fc.setUID((MainApp.fc.getDeviceID() + MainApp.fc.get_ID()));
            db.updateFormID();
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void SaveDraft() throws JSONException {
        MainApp.fc = new FormsContract();
        MainApp.fc.setDevicetagID(getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null));
        MainApp.fc.setFormDate(new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime()));
        MainApp.fc.setUser(MainApp.userName);
        MainApp.fc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        MainApp.fc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        settingGPS(MainApp.fc);

        JSONObject child = new JSONObject();
        child.put("tcvcl01", bi.tcvcl01.getText().toString());
        child.put("tcvcl02", bi.tcvcl02.getText().toString());
        child.put("tcvcl03", bi.tcvcl03.getText().toString());
        child.put("tcvcl04", bi.tcvcl04.getText().toString());
        child.put("tcvcl05", bi.tcvcl05a.isChecked() ? "1" : bi.tcvcl05b.isChecked() ? "2" : "0");
        child.put("tcvcl06", bi.tcvcl06.getText().toString());
        child.put("tcvcl07", bi.tcvcl07.getText().toString());
        child.put("tcvcl08", bi.tcvcl08.getText().toString());
        child.put("tcvcl09", bi.tcvcl09.getText().toString());
        child.put("tcvcl10", bi.tcvcl10.getText().toString());
        child.put("tcvcl11", bi.tcvcl11a.isChecked() ? "1" : bi.tcvcl11b.isChecked() ? "2" : "0");
        child.put("tcvcl12", bi.tcvcl12a.isChecked() ? "1" : bi.tcvcl12b.isChecked() ? "2" : "0");
        child.put("tcvcl13", bi.tcvcl13a.isChecked() ? "1" : bi.tcvcl13b.isChecked() ? "2" : "0");
        child.put("tcvcl14", bi.tcvcl14a.isChecked() ? "1" : bi.tcvcl14b.isChecked() ? "2" : "0");
        child.put("tcvcl15", bi.tcvcl15a.isChecked() ? "1" : bi.tcvcl15b.isChecked() ? "2" : "0");
        child.put("tcvcl16", bi.tcvcl16a.isChecked() ? "1" : bi.tcvcl16b.isChecked() ? "2" : "0");
        child.put("tcvcl17", bi.tcvcl17a.isChecked() ? "1" : bi.tcvcl17b.isChecked() ? "2" : "0");
        child.put("tcvcl18", bi.tcvcl18.getText().toString());
        child.put("tcvcl19", bi.tcvcl19.getText().toString());
        child.put("tcvcl20", bi.tcvcl20.getText().toString());

        MainApp.fc.setchild(String.valueOf(child));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.childSec);
    }

    public void BtnEnd() {
        MainApp.endActivity(this, this, false);
    }

    public void settingGPS(FormsContract fc) {
        MainApp.LocClass locClass = MainApp.setGPS(this);
        fc.setGpsLat(locClass.getLatitude());
        fc.setGpsLng(locClass.getLongitude());
        fc.setGpsAcc(locClass.getAccuracy());
        fc.setGpsDT(locClass.getTime());
    }

}
