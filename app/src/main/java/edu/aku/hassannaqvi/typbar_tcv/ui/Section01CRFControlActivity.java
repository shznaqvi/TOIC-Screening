package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection01CrfControlBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section01CRFControlActivity extends AppCompatActivity {

    ActivitySection01CrfControlBinding bi;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section01_crf_control);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setListeners() {

        bi.tcvscla02.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvscla02a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrp0130, null);
                }
            }
        });
        bi.tcvscla03.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvscla03a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrp0130, null);
                }
            }
        });
        bi.tcvscla04.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvscla04a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrp0130, null);
                }
            }
        });
        bi.tcvscla05.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvscla05a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrp0130, null);
                }
            }
        });
        bi.tcvscla06.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvscla06a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrp0130, null);
                }
            }
        });
        bi.tcvsclc15.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc15a.isChecked())
                    bi.tcvsclc15a01.clearCheck();

            }
        });

        bi.tcvsclc17.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc1797.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc18, null);
                }
            }
        });

        bi.tcvsclc07.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId != bi.tcvsclc07c.getId()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc08, null);
                }
            }
        });


    }

    private void setContentUI() {
        this.setTitle(R.string.CrfControl);

        // Initialize db
        db = new DatabaseHelper(this);
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

            if (bi.tcvscla02a.isChecked() && bi.tcvscla03a.isChecked() && bi.tcvscla04a.isChecked() && bi.tcvscla05a.isChecked() && bi.tcvscla06a.isChecked()) {
                startActivity(new Intent(this, Section02CRFControlActivity.class));
            } else {
                startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false));
            }

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
        MainApp.fc.setFormtype("scl");

        JSONObject CrfControl = new JSONObject();

        CrfControl.put("tcvscla02", bi.tcvscla02a.isChecked() ? "1" : bi.tcvscla02b.isChecked() ? "2" : "0");
        CrfControl.put("tcvscla03", bi.tcvscla03a.isChecked() ? "1" : bi.tcvscla03b.isChecked() ? "2" : "0");
        CrfControl.put("tcvscla04", bi.tcvscla04a.isChecked() ? "1" : bi.tcvscla04b.isChecked() ? "2" : "0");
        CrfControl.put("tcvscla05", bi.tcvscla05a.isChecked() ? "1" : bi.tcvscla05b.isChecked() ? "2" : "0");
        CrfControl.put("tcvscla06", bi.tcvscla06a.isChecked() ? "1" : bi.tcvscla06b.isChecked() ? "2" : "0");

        CrfControl.put("tcvsclb01", bi.tcvsclb01.getText().toString());
        CrfControl.put("tcvsclb02", bi.tcvsclb02.getText().toString());
        CrfControl.put("tcvsclb03", bi.tcvsclb03.getText().toString());
        CrfControl.put("tcvsclb04", bi.tcvsclb04.getText().toString());

        CrfControl.put("tcvsclb05Age", bi.tcvsclb05Agea.isChecked() ? "1" : bi.tcvsclb05Ageb.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclb05", bi.tcvsclb05.getText().toString());
        CrfControl.put("tcvsclb06", bi.tcvsclb06.getText().toString());

        CrfControl.put("tcvsclb07", bi.tcvsclb07a.isChecked() ? "1" : bi.tcvsclb07b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclb08", bi.tcvsclb08.getText().toString());
        CrfControl.put("tcvsclb09", bi.tcvsclb09.getText().toString());
        CrfControl.put("tcvsclb10", bi.tcvsclb10.getText().toString());
        CrfControl.put("tcvsclb10", bi.tcvsclb11a.isChecked() ? "1" : bi.tcvsclb11b.isChecked() ? "2" : "0");

        /*New question added in between form*/

        CrfControl.put("tcvsclc32", bi.tcvsclc02.getText().toString());
        CrfControl.put("tcvsclc33", bi.tcvsclc02.getText().toString());

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

        MainApp.fc.setsA(String.valueOf(CrfControl));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.llcrfControl);
    }

    public void BtnEnd() {
        try {
            if (!ValidatorClass.EmptyCheckingContainer(this, bi.ll0105)) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false));

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
