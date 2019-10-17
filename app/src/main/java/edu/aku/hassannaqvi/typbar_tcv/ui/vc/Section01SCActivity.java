package edu.aku.hassannaqvi.typbar_tcv.ui.vc;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
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
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection01ScBinding;
import edu.aku.hassannaqvi.typbar_tcv.other.CheckingIDCC;
import edu.aku.hassannaqvi.typbar_tcv.ui.EndingActivity;
import edu.aku.hassannaqvi.typbar_tcv.ui.Section00CRFCaseActivity;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section01SCActivity extends AppCompatActivity {

    ActivitySection01ScBinding bi;
    Map<String, SchoolContract> schoolMap;
    Map<String, String> ucMap;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section01_sc);
        bi.setCallback(this);

        setListeners();

    }

    private void setListeners() {

        bi.tcvcsa09.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (bi.tcvcsa09b.isChecked()) {
                    ClearClass.ClearAllFields(bi.llvcsa01, null);
                }
            }
        });

        bi.tcvcsa11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (bi.tcvcsa11b.isChecked()) {
                    ClearClass.ClearAllFields(bi.llvcsa01, null);
                }
            }
        });
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
            finish();
            startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

        DatabaseHelper db = new DatabaseHelper(this);
        long updcount = db.addForm(MainApp.fc);
        if (updcount > 0) {
            MainApp.fc.set_ID(String.valueOf(updcount));
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
        MainApp.fc.setFormtype(MainApp.SCHOOLLISTINGTYPE);

        JSONObject f1 = new JSONObject();

        f1.put("tcvcsa01", bi.tcvcsa01.getText().toString());
        f1.put("tcvcsa02", bi.tcvcsa02.getText().toString());
        f1.put("tcvcsa03", bi.tcvcsa03.getText().toString());
        f1.put("tcvcsa04", bi.tcvcsa04.getText().toString());
        f1.put("tcvcsa05", bi.tcvcsa05.getText().toString());
        f1.put("tcvcsa06", bi.tcvcsa06.getText().toString());
        f1.put("tcvcsa07", bi.tcvcsa07.getText().toString());
        f1.put("tcvcsa08", bi.tcvcsa08a.isChecked() ? "1" : bi.tcvcsa08b.isChecked() ? "2" : "0");
        f1.put("tcvcsa09", bi.tcvcsa09a.isChecked() ? "1" : bi.tcvcsa09b.isChecked() ? "2" : "0");
        f1.put("tcvcsa10", bi.tcvcsa10.getText().toString());
        f1.put("tcvcsa11", bi.tcvcsa11a.isChecked() ? "1" : bi.tcvcsa11b.isChecked() ? "2" : "0");
        f1.put("tcvcsa13", bi.tcvcsa13a.isChecked() ? "1" : bi.tcvcsa13b.isChecked() ? "2" : "0");
        f1.put("tcvcsa1496x", bi.tcvcsa1496x.getText().toString());
        f1.put("tcvcsa14", bi.tcvcsa14a.isChecked() ? "1" : bi.tcvcsa14b.isChecked() ? "2" : bi.tcvcsa14c.isChecked() ? "3" : bi.tcvcsa1496.isChecked() ? "96" : "0");
        f1.put("tcvcsa15", bi.tcvcsa15a.isChecked() ? "1" : bi.tcvcsa15b.isChecked() ? "2" : bi.tcvcsa15c.isChecked() ? "3" : bi.tcvcsa1598.isChecked() ? "98" : "0");

        MainApp.fc.setsA(String.valueOf(f1));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpSecA01);
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
