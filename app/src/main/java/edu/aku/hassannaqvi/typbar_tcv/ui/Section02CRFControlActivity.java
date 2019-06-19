package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection02CrfcontrolBinding;
import edu.aku.hassannaqvi.typbar_tcv.utils.JsonUtils;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

import static edu.aku.hassannaqvi.typbar_tcv.core.MainApp.fc;

public class Section02CRFControlActivity extends AppCompatActivity {

    ActivitySection02CrfcontrolBinding bi;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section02_crfcontrol);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setListeners() {

        bi.tcvsclc28.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc28a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc29, null);
                }
            }
        });
        bi.tcvsclc27.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc27a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc28, null);
                }
            }
        });

        bi.tcvsclc20.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc2097.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc21, null);
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
//        MainApp.fc = new FormsContract();
//        MainApp.fc.setDevicetagID(getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null));
//        MainApp.fc.setFormDate(new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime()));
//        MainApp.fc.setUser(MainApp.userName);
//        MainApp.fc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
//                Settings.Secure.ANDROID_ID));
//        MainApp.fc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
//        settingGPS(MainApp.fc);
//        MainApp.fc.setFormtype("scl");

        JSONObject CrfControl = new JSONObject();

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

        CrfControl.put("tcvsclc2401", bi.tcvsclc2401a.isChecked() ? "1" : bi.tcvsclc2401b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2402", bi.tcvsclc2402a.isChecked() ? "1" : bi.tcvsclc2402b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2403", bi.tcvsclc2403a.isChecked() ? "1" : bi.tcvsclc2403b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2404", bi.tcvsclc2404a.isChecked() ? "1" : bi.tcvsclc2404b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2405", bi.tcvsclc2405a.isChecked() ? "1" : bi.tcvsclc2405b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2406", bi.tcvsclc2406a.isChecked() ? "1" : bi.tcvsclc2406b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2496", bi.tcvsclc2496a.isChecked() ? "1" : bi.tcvsclc2496b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2496x", bi.tcvsclc2496x.getText().toString());


        CrfControl.put("tcvsclc2501", bi.tcvsclc2501a.isChecked() ? "1" : bi.tcvsclc2501b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2502", bi.tcvsclc2502a.isChecked() ? "1" : bi.tcvsclc2502b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2503", bi.tcvsclc2503a.isChecked() ? "1" : bi.tcvsclc2503b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2504", bi.tcvsclc2504a.isChecked() ? "1" : bi.tcvsclc2504b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2505", bi.tcvsclc2505a.isChecked() ? "1" : bi.tcvsclc2505b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2506", bi.tcvsclc2506a.isChecked() ? "1" : bi.tcvsclc2506b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2507", bi.tcvsclc2507a.isChecked() ? "1" : bi.tcvsclc2507b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2596", bi.tcvsclc2596a.isChecked() ? "1" : bi.tcvsclc2596b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2596x", bi.tcvsclc2596x.getText().toString());


        CrfControl.put("tcvsclc2601", bi.tcvsclc2601a.isChecked() ? "1" : bi.tcvsclc2601b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2602", bi.tcvsclc2602a.isChecked() ? "1" : bi.tcvsclc2602b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2603", bi.tcvsclc2603a.isChecked() ? "1" : bi.tcvsclc2603b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2604", bi.tcvsclc2604a.isChecked() ? "1" : bi.tcvsclc2604b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2605", bi.tcvsclc2605a.isChecked() ? "1" : bi.tcvsclc2605b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2606", bi.tcvsclc2606a.isChecked() ? "1" : bi.tcvsclc2606b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2607", bi.tcvsclc2607a.isChecked() ? "1" : bi.tcvsclc2607b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2608", bi.tcvsclc2608a.isChecked() ? "1" : bi.tcvsclc2608b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2609", bi.tcvsclc2609a.isChecked() ? "1" : bi.tcvsclc2609b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2696", bi.tcvsclc2696a.isChecked() ? "1" : bi.tcvsclc2696b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2696x", bi.tcvsclc2696x.getText().toString());


        CrfControl.put("tcvsclc27", bi.tcvsclc27a.isChecked() ? "1"
                : bi.tcvsclc27b.isChecked() ? "2"
                : bi.tcvsclc2798.isChecked() ? "98"
                : "0");

        CrfControl.put("tcvsclc28", bi.tcvsclc28a.isChecked() ? "1"
                : bi.tcvsclc28b.isChecked() ? "2"
                : "0");

        CrfControl.put("tcvsclc29", bi.tcvsclc29.getText().toString());

        CrfControl.put("tcvsclc30", bi.tcvsclc30.getText().toString());

        try {
            JSONObject s4_merge = JsonUtils.mergeJSONObjects(new JSONObject(fc.getsA()), CrfControl);

            fc.setsA(String.valueOf(s4_merge));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.llcrfControl);
    }

    public void BtnEnd() {
        try {

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


}
