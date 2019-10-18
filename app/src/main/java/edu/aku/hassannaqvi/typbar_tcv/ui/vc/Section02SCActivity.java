package edu.aku.hassannaqvi.typbar_tcv.ui.vc;

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
import edu.aku.hassannaqvi.typbar_tcv.contracts.MembersContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection02ScBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section02SCActivity extends AppCompatActivity {

    ActivitySection02ScBinding bi;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section02_sc);
        bi.setCallback(this);

        setListeners();

    }

    private void setListeners() {

        bi.tcvcsb06.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (bi.tcvcsb0698.isChecked()) {
                    ClearClass.ClearAllFields(bi.llvcsb01, null);
                }
            }
        });

        bi.tcvcsb07.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (bi.tcvcsb07c.isChecked()) {
                    ClearClass.ClearAllFields(bi.llvcsb02, null);
                }
            }
        });
    }

    public void BtnContinue() {
        try {

            if (!formValidation()) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            }
            finish();
            startActivity(new Intent(this, Section03SCActivity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

        DatabaseHelper db = new DatabaseHelper(this);
        long updcount = db.addMemberForms(MainApp.cc);
        if (updcount > 0) {
            MainApp.cc.set_ID(String.valueOf(updcount));
            MainApp.cc.setUID((MainApp.cc.getDeviceID() + MainApp.cc.get_ID()));
            db.updateMemberID();

            return true;
        }

        return false;
    }

    private void SaveDraft() throws JSONException {
        MainApp.cc = new MembersContract();
        MainApp.cc.setDevicetagID(getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null));
        MainApp.cc.setFormDate(new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime()));
        MainApp.cc.setUser(MainApp.userName);
        MainApp.cc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        MainApp.cc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        MainApp.cc.setUUID(MainApp.fc.getUID());

        JSONObject c1 = new JSONObject();




        c1.put("tcvcsb01", bi.tcvcsb01.getText().toString());
        c1.put("tcvcsb02", bi.tcvcsb02a.isChecked() ? "1" : bi.tcvcsb02b.isChecked() ? "2" : "0");

        c1.put("tcvcsb03Age", bi.tcvcsbDOB.isChecked() ? "1" : bi.tcvcsbA.isChecked() ? "2" : "0");
        c1.put("tcvcsb03", bi.tcvcsb03.getText().toString());
        c1.put("tcvcsb04y", bi.tcvcsb04y.getText().toString());
        c1.put("tcvcsb04m", bi.tcvcsb04m.getText().toString());

        c1.put("tcvcsb0596x", bi.tcvcsb0596x.getText().toString());
        c1.put("tcvcsb05", bi.tcvcsb05a.isChecked() ? "1" : bi.tcvcsb05b.isChecked() ? "2" : bi.tcvcsb0596.isChecked() ? "96" : "0");
        c1.put("tcvcsb06", bi.tcvcsb06a.isChecked() ? "1" : bi.tcvcsb06b.isChecked() ? "2" : bi.tcvcsb0698.isChecked() ? "98" : "0");
        c1.put("tcvcsb07", bi.tcvcsb07a.isChecked() ? "1" : bi.tcvcsb07b.isChecked() ? "2" : bi.tcvcsb07c.isChecked() ? "3" : "0");
        c1.put("tcvcsb08d", bi.tcvcsb08d.getText().toString());
        c1.put("tcvcsb09", bi.tcvcsb09.getText().toString());
        c1.put("tcvcsb1096x", bi.tcvcsb1096x.getText().toString());
        c1.put("tcvcsb10", bi.tcvcsb10a.isChecked() ? "1" : bi.tcvcsb10b.isChecked() ? "2" : bi.tcvcsb10c.isChecked() ? "3" : bi.tcvcsb10d.isChecked() ? "4" : bi.tcvcsb10e.isChecked() ? "5" : bi.tcvcsb1096.isChecked() ? "96" : "0");
        c1.put("tcvcsb11", bi.tcvcsb11a.isChecked() ? "1" : bi.tcvcsb11b.isChecked() ? "2" : bi.tcvcsb11c.isChecked() ? "3" : bi.tcvcsb11d.isChecked() ? "4" : "0");
        c1.put("tcvcsb12h", bi.tcvcsb12h.getText().toString());
        c1.put("tcvcsb12m", bi.tcvcsb12m.getText().toString());
        c1.put("tcvcsb13961x", bi.tcvcsb13961x.getText().toString());
        c1.put("tcvcsb13962x", bi.tcvcsb13962x.getText().toString());
        c1.put("tcvcsb13963x", bi.tcvcsb13963x.getText().toString());
        c1.put("tcvcsb13", bi.tcvcsb13a.isChecked() ? "1" : bi.tcvcsb13b.isChecked() ? "2" : bi.tcvcsb13961.isChecked() ? "961" : bi.tcvcsb13c.isChecked() ? "3" : bi.tcvcsb13d.isChecked() ? "4" : bi.tcvcsb13962.isChecked() ? "962" : bi.tcvcsb13e.isChecked() ? "5" : bi.tcvcsb13f.isChecked() ? "6" : bi.tcvcsb13g.isChecked() ? "7" : bi.tcvcsb13h.isChecked() ? "8" : bi.tcvcsb13i.isChecked() ? "9" : bi.tcvcsb13963.isChecked() ? "963" : "0");

        MainApp.cc.setsB(String.valueOf(c1));

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
