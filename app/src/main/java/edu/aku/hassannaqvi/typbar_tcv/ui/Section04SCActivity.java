package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.UCsContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection04ScBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section04SCActivity extends AppCompatActivity {

    ActivitySection04ScBinding bi;
    Map<String, SchoolContract> schoolMap;
    Map<String, String> ucMap;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section04_sc);
        bi.setCallback(this);

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

        f1.put("tcvcsc26a",bi.tcvcsc26a.isChecked() ?"1" :"0");
        f1.put("tcvcsc26b",bi.tcvcsc26b.isChecked() ?"2" :"0");
        f1.put("tcvcsc26c",bi.tcvcsc26c.isChecked() ?"3" :"0");
        f1.put("tcvcsc26d",bi.tcvcsc26d.isChecked() ?"4" :"0");
        f1.put("tcvcsc26e",bi.tcvcsc26e.isChecked() ?"5" :"0");
        f1.put("tcvcsc26f",bi.tcvcsc26f.isChecked() ?"6" :"0");
        f1.put("tcvcsc26g",bi.tcvcsc26g.isChecked() ?"7" :"0");
        f1.put("tcvcsc26h",bi.tcvcsc26h.isChecked() ?"8" :"0");
        f1.put("tcvcsc26i",bi.tcvcsc26i.isChecked() ?"9" :"0");
        f1.put("tcvcsc26j",bi.tcvcsc26j.isChecked() ?"10" :"0");
        f1.put("tcvcsc26k",bi.tcvcsc26k.isChecked() ?"11" :"0");
        f1.put("tcvcsc26l",bi.tcvcsc26l.isChecked() ?"12" :"0");
        f1.put("tcvcsc26m",bi.tcvcsc26m.isChecked() ?"13" :"0");
        f1.put("tcvcsc27a",bi.tcvcsc27a.isChecked() ?"1" :"0");
        f1.put("tcvcsc27b",bi.tcvcsc27b.isChecked() ?"2" :"0");
        f1.put("tcvcsc27c",bi.tcvcsc27c.isChecked() ?"3" :"0");
        f1.put("tcvcsc27d",bi.tcvcsc27d.isChecked() ?"4" :"0");
        f1.put("tcvcsc27e",bi.tcvcsc27e.isChecked() ?"5" :"0");
        f1.put("tcvcsc27f",bi.tcvcsc27f.isChecked() ?"6" :"0");
        f1.put("tcvcsc28a",bi.tcvcsc28a.isChecked() ?"1" :"0");
        f1.put("tcvcsc28b",bi.tcvcsc28b.isChecked() ?"2" :"0");
        f1.put("tcvcsc28c",bi.tcvcsc28c.isChecked() ?"3" :"0");
        f1.put("tcvcsc28d",bi.tcvcsc28d.isChecked() ?"4" :"0");
        f1.put("tcvcsc28e",bi.tcvcsc28e.isChecked() ?"5" :"0");
        f1.put("tcvcsc28f",bi.tcvcsc28f.isChecked() ?"6" :"0");
        f1.put("tcvcsc2896",bi.tcvcsc2896.isChecked() ?"96" :"0");
        f1.put("tcvcsc2896x", bi.tcvcsc2896x.getText().toString());
        f1.put("tcvcsc29a",bi.tcvcsc29a.isChecked() ?"1" :"0");
        f1.put("tcvcsc29b",bi.tcvcsc29b.isChecked() ?"2" :"0");
        f1.put("tcvcsc29c",bi.tcvcsc29c.isChecked() ?"3" :"0");
        f1.put("tcvcsc29d",bi.tcvcsc29d.isChecked() ?"4" :"0");
        f1.put("tcvcsc29e",bi.tcvcsc29e.isChecked() ?"5" :"0");
        f1.put("tcvcsc29f",bi.tcvcsc29f.isChecked() ?"6" :"0");
        f1.put("tcvcsc2996",bi.tcvcsc2996.isChecked() ?"96" :"0");
        f1.put("tcvcsc2996x", bi.tcvcsc2996x.getText().toString());
        f1.put("tcvcsc30",bi.tcvcsc30a.isChecked() ?"1" :bi.tcvcsc30b.isChecked() ?"2" :bi.tcvcsc30c.isChecked() ?"3" :bi.tcvcsc30d.isChecked() ?"4" :bi.tcvcsc30e.isChecked() ?"5" :"0");
        f1.put("tcvcsc31",bi.tcvcsc31a.isChecked() ?"1" :bi.tcvcsc31b.isChecked() ?"2" :bi.tcvcsc31c.isChecked() ?"3" :bi.tcvcsc31d.isChecked() ?"4" :bi.tcvcsc31e.isChecked() ?"5" :"0");
        f1.put("tcvcsc32",bi.tcvcsc32a.isChecked() ?"1" :bi.tcvcsc32b.isChecked() ?"2" :bi.tcvcsc32c.isChecked() ?"3" :bi.tcvcsc32d.isChecked() ?"4" :bi.tcvcsc32e.isChecked() ?"5" :"0");
        f1.put("tcvcsc33",bi.tcvcsc33a.isChecked() ?"1" :bi.tcvcsc33b.isChecked() ?"2" :bi.tcvcsc33c.isChecked() ?"3" :bi.tcvcsc33d.isChecked() ?"4" :bi.tcvcsc33e.isChecked() ?"5" :"0");

        MainApp.fc.setsA(String.valueOf(f1));

    }

    private boolean formValidation() {
        if (!ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpSecA01)) return false;

        return true;
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
