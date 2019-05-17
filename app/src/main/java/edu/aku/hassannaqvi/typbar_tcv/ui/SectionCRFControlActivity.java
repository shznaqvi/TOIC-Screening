package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySectionCrfControlBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class SectionCRFControlActivity extends AppCompatActivity {

    ActivitySectionCrfControlBinding bi;
    DatabaseHelper db;
    Map<String, SchoolContract> schoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_crf_control);
        bi.setCallback(this);

        setContentUI();
        //setListeners();
    }

    private void setContentUI() {
        this.setTitle(R.string.CrfControl);

        // Initialize db
        db = new DatabaseHelper(this);
        //filledSpinners();
    }



    public void BtnContinue() {
        try {

            if (!formValidation()) return;

//            if (!MainApp.checkingGPSRules(this)) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));

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

            return true;
        }

        return false;
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
        MainApp.fc.setFormtype("cl");

        JSONObject CrfControl = new JSONObject();

        //SectionCRFControlActivity.put("tcvcl00", bi.tcvcl00.getSelectedItem());

        //child.put("sch_code", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_code());
        //child.put("sch_add", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_add());
        //child.put("sch_type", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_type());
        //child.put("tcvcl01", bi.tcvcl01.getSelectedItem());


        CrfControl.put("tcvscla01", bi.tcvscla01a.isChecked() ? "1" : bi.tcvscla01b.isChecked() ? "2" : "0");
        CrfControl.put("tcvscla02", bi.tcvscla02a.isChecked() ? "1" : bi.tcvscla02b.isChecked() ? "2" : "0");
        CrfControl.put("tcvscla03", bi.tcvscla03a.isChecked() ? "1" : bi.tcvscla03b.isChecked() ? "2" : "0");
        CrfControl.put("tcvscla04", bi.tcvscla04a.isChecked() ? "1" : bi.tcvscla04b.isChecked() ? "2" : "0");
        CrfControl.put("tcvscla05", bi.tcvscla05a.isChecked() ? "1" : bi.tcvscla05b.isChecked() ? "2" : "0");

        CrfControl.put("tcvsclb01", bi.tcvsclb01.getText().toString());
        CrfControl.put("tcvsclb02", bi.tcvsclb02.getText().toString());
        CrfControl.put("tcvsclb03", bi.tcvsclb03.getText().toString());
        CrfControl.put("tcvsclb04", bi.tcvsclb04.getText().toString());
        CrfControl.put("tcvsclb05", bi.tcvsclb05.getText().toString());
        CrfControl.put("tcvsclb06", bi.tcvsclb06.getText().toString());
        CrfControl.put("tcvsclb07", bi.tcvsclb07.getText().toString());
        CrfControl.put("tcvsclb08", bi.tcvsclb08a.isChecked() ? "1" : bi.tcvsclb08b.isChecked() ? "2" : "0");
        
        CrfControl.put("tcvsclc01", bi.tcvsclc01a.isChecked() ? "1" 
                : bi.tcvsclc01b.isChecked() ? "2" 
                : bi.tcvsclc01c.isChecked() ? "3" 
                : "0");
        
        CrfControl.put("tcvsclc02", bi.tcvsclc02.getText().toString());
        
        CrfControl.put("tcvsclc03", bi.tcvsclc03a.isChecked() ? "1" 
                : bi.tcvsclc03b.isChecked() ? "2" 
                : bi.tcvsclc03c.isChecked() ? "3" 
                : bi.tcvsclc0396.isChecked() ? "96" 
                : "0");
        CrfControl.put("tcvsclc0396x", bi.tcvsclc0396x.getText().toString());
        
        CrfControl.put("tcvsclc04", bi.tcvsclc04a.isChecked() ? "1" 
                : bi.tcvsclc04b.isChecked() ? "2" 
                : bi.tcvsclc04c.isChecked() ? "3" 
                : bi.tcvsclc0496.isChecked() ? "96" 
                : "0");
        CrfControl.put("tcvsclc0496x", bi.tcvsclc0496x.getText().toString());
        
        CrfControl.put("tcvsclc05", bi.tcvsclc05a.isChecked() ? "1"
                : bi.tcvsclc05b.isChecked() ? "2"
                : bi.tcvsclc05c.isChecked() ? "3"
                : bi.tcvsclc0596.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc0596x", bi.tcvsclc0596x.getText().toString());
        
        CrfControl.put("tcvsclc0601", bi.tcvsclc0601.isChecked() ? "1" : "0");
        CrfControl.put("tcvsclc0602", bi.tcvsclc0602.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc0603", bi.tcvsclc0603.isChecked() ? "3" : "0");
        CrfControl.put("tcvsclc0604", bi.tcvsclc0604.isChecked() ? "4" : "0");
        CrfControl.put("tcvsclc0605", bi.tcvsclc0605.isChecked() ? "5" : "0");
        CrfControl.put("tcvsclc0606", bi.tcvsclc0606.isChecked() ? "6" : "0");
        CrfControl.put("tcvsclc0696", bi.tcvsclc0696.isChecked() ? "96" : "0");
        CrfControl.put("tcvsclc0696x", bi.tcvsclc0696x.getText().toString());
        
        CrfControl.put("tcvsclc07", bi.tcvsclc07a.isChecked() ? "1"
                : bi.tcvsclc07b.isChecked() ? "2"
                : bi.tcvsclc07c.isChecked() ? "3"
                : bi.tcvsclc07d.isChecked() ? "4"
                : bi.tcvsclc07e.isChecked() ? "5"
                : bi.tcvsclc0796.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc0796x", bi.tcvsclc0796x.getText().toString());

        CrfControl.put("tcvsclc08", bi.tcvsclc08a.isChecked() ? "1" 
                : bi.tcvsclc08b.isChecked() ? "2" 
                : bi.tcvsclc08c.isChecked() ? "3" 
                : bi.tcvsclc08d.isChecked() ? "4" 
                : "0");
        
        CrfControl.put("tcvsclc09", bi.tcvsclc09a.isChecked() ? "1" 
                : bi.tcvsclc09b.isChecked() ? "2" 
                : bi.tcvsclc09c.isChecked() ? "3" 
                : "0");
        
        CrfControl.put("tcvsclc10", bi.tcvsclc10a.isChecked() ? "1" 
                : bi.tcvsclc10b.isChecked() ? "2" 
                : bi.tcvsclc10c.isChecked() ? "3" 
                : bi.tcvsclc10d.isChecked() ? "4" 
                : bi.tcvsclc1096.isChecked() ? "96" 
                : "0");
        CrfControl.put("tcvsclc1096x", bi.tcvsclc1096x.getText().toString());
        
        CrfControl.put("tcvsclc11", bi.tcvsclc11a.isChecked() ? "1" 
                : bi.tcvsclc11b.isChecked() ? "2" 
                : bi.tcvsclc11c.isChecked() ? "3" 
                : bi.tcvsclc1196.isChecked() ? "96" 
                : "0");
        CrfControl.put("tcvsclc1196x", bi.tcvsclc1196x.getText().toString());
        
        CrfControl.put("tcvsclc12", bi.tcvsclc12a.isChecked() ? "1" 
                : bi.tcvsclc12b.isChecked() ? "2" 
                : bi.tcvsclc1297.isChecked() ? "97" 
                : "0");
        
        CrfControl.put("tcvsclc13", bi.tcvsclc13a.isChecked() ? "1" 
                : bi.tcvsclc13b.isChecked() ? "2" 
                : bi.tcvsclc1396.isChecked() ? "96" 
                : "0");
        CrfControl.put("tcvsclc1396x", bi.tcvsclc1396x.getText().toString());
        CrfControl.put("tcvsclc13ax", bi.tcvsclc13ax.getText().toString());
        
        CrfControl.put("tcvsclc14", bi.tcvsclc14a.isChecked() ? "1"
                : bi.tcvsclc14b.isChecked() ? "2"
                : bi.tcvsclc1497.isChecked() ? "97"
                : "0");

        CrfControl.put("tcvsclc15", bi.tcvsclc15a.isChecked() ? "1"
                : bi.tcvsclc15b.isChecked() ? "2"
                : bi.tcvsclc1598.isChecked() ? "98"
                : "0");

        CrfControl.put("tcvsclc15a01", bi.tcvsclc15a01a.isChecked() ? "1"
                : bi.tcvsclc15a01b.isChecked() ? "2"
                : bi.tcvsclc15a0198.isChecked() ? "98"
                : "0");
        
        CrfControl.put("tcvsclc16", bi.tcvsclc16a.isChecked() ? "1" 
                : bi.tcvsclc16b.isChecked() ? "2" 
                : bi.tcvsclc16c.isChecked() ? "3" 
                : bi.tcvsclc16d.isChecked() ? "4" 
                : "0");
        
        CrfControl.put("tcvsclc17", bi.tcvsclc17a.isChecked() ? "1" 
                : bi.tcvsclc17b.isChecked() ? "2" 
                : bi.tcvsclc1797.isChecked() ? "97" 
                : "0");
        
        CrfControl.put("tcvsclc18", bi.tcvsclc18a.isChecked() ? "1" 
                : bi.tcvsclc18b.isChecked() ? "2" 
                : bi.tcvsclc1897.isChecked() ? "97" 
                : "0");
        
        CrfControl.put("tcvsclc19", bi.tcvsclc19a.isChecked() ? "1" 
                : bi.tcvsclc19b.isChecked() ? "2" 
                : bi.tcvsclc19c.isChecked() ? "3" 
                : bi.tcvsclc19d.isChecked() ? "4" 
                : bi.tcvsclc19e.isChecked() ? "5" 
                : bi.tcvsclc19f.isChecked() ? "6" 
                : bi.tcvsclc1997.isChecked() ? "97" 
                : "0");
        
        CrfControl.put("tcvsclc20", bi.tcvsclc20a.isChecked() ? "1" 
                : bi.tcvsclc20b.isChecked() ? "2" 
                : bi.tcvsclc2097.isChecked() ? "97" 
                : "0");
        
        CrfControl.put("tcvsclc21", bi.tcvsclc21a.isChecked() ? "1" 
                : bi.tcvsclc21b.isChecked() ? "2" 
                : bi.tcvsclc2196.isChecked() ? "96" 
                : "0");
        CrfControl.put("tcvsclc2196x", bi.tcvsclc2196x.getText().toString());
        
        CrfControl.put("tcvsclc22", bi.tcvsclc22a.isChecked() ? "1" 
                : bi.tcvsclc22b.isChecked() ? "2" 
                : bi.tcvsclc2297.isChecked() ? "97" 
                : "0");
        
        CrfControl.put("tcvsclc23", bi.tcvsclc23a.isChecked() ? "1" 
                : bi.tcvsclc23b.isChecked() ? "2" 
                : "0");

        CrfControl.put("tcvsclc2401", bi.tcvsclc2401.isChecked() ? "1" : "0");
        CrfControl.put("tcvsclc2402", bi.tcvsclc2402.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2403", bi.tcvsclc2403.isChecked() ? "3" : "0");
        CrfControl.put("tcvsclc2404", bi.tcvsclc2404.isChecked() ? "4" : "0");
        CrfControl.put("tcvsclc2405", bi.tcvsclc2405.isChecked() ? "5" : "0");
        CrfControl.put("tcvsclc2406", bi.tcvsclc2406.isChecked() ? "6" : "0");
        CrfControl.put("tcvsclc2496", bi.tcvsclc2496.isChecked() ? "96" : "0");
        CrfControl.put("tcvsclc2496x", bi.tcvsclc2496x.getText().toString());

        CrfControl.put("tcvsclc2501", bi.tcvsclc2501.isChecked() ? "1" : "0");
        CrfControl.put("tcvsclc2502", bi.tcvsclc2502.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2503", bi.tcvsclc2503.isChecked() ? "3" : "0");
        CrfControl.put("tcvsclc2504", bi.tcvsclc2504.isChecked() ? "4" : "0");
        CrfControl.put("tcvsclc2505", bi.tcvsclc2505.isChecked() ? "5" : "0");
        CrfControl.put("tcvsclc2596", bi.tcvsclc2596.isChecked() ? "96" : "0");
        CrfControl.put("tcvsclc2596x", bi.tcvsclc2596x.getText().toString());

        CrfControl.put("tcvsclc2601", bi.tcvsclc2601.isChecked() ? "1" : "0");
        CrfControl.put("tcvsclc2602", bi.tcvsclc2602.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2603", bi.tcvsclc2603.isChecked() ? "3" : "0");
        CrfControl.put("tcvsclc2604", bi.tcvsclc2604.isChecked() ? "4" : "0");
        CrfControl.put("tcvsclc2605", bi.tcvsclc2605.isChecked() ? "5" : "0");
        CrfControl.put("tcvsclc2606", bi.tcvsclc2606.isChecked() ? "6" : "0");
        CrfControl.put("tcvsclc2607", bi.tcvsclc2607.isChecked() ? "7" : "0");
        CrfControl.put("tcvsclc2608", bi.tcvsclc2608.isChecked() ? "8" : "0");

        CrfControl.put("tcvsclc27", bi.tcvsclc27a.isChecked() ? "1" 
                : bi.tcvsclc27b.isChecked() ? "2" 
                : bi.tcvsclc2798.isChecked() ? "98" 
                : "0");
        
        CrfControl.put("tcvsclc28", bi.tcvsclc28a.isChecked() ? "1" 
                : bi.tcvsclc28b.isChecked() ? "2" 
                : "0");

        CrfControl.put("tcvsclc29", bi.tcvsclc29.getText().toString());
        
        CrfControl.put("tcvsclc30", bi.tcvsclc30.getText().toString());

        MainApp.fc.setsA(String.valueOf(CrfControl));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.llcrfControl);
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

    //

}
