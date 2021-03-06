package edu.aku.hassannaqvi.typbar_tcv.ui.vc;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.MembersContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.UCsContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection01ScBinding;
import edu.aku.hassannaqvi.typbar_tcv.other.CheckingIDCC;
import edu.aku.hassannaqvi.typbar_tcv.ui.EndingActivity;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section01SCActivity extends AppCompatActivity {

    ActivitySection01ScBinding bi;
    Map<String, SchoolContract> schoolMap;
    Map<String, String> ucMap;
    DatabaseHelper db;
    public static HashMap<String, MembersContract.FamilyTableVC> mothersMap;
    public static ArrayList<String> mothersName;
    public static int motherCounter, childCount, childCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section01_sc);
        bi.setCallback(this);

        setListeners();

        db = new DatabaseHelper(this);
        ArrayList<String> ucsNames = new ArrayList<>();
        ucsNames.add("....");
        ArrayList<UCsContract> ucsContract = db.getAllUCs();
        ucMap = new HashMap<>();
        for (UCsContract uc : ucsContract) {
            ucMap.put(uc.getUcsName(), uc.getUccode());
            ucsNames.add(uc.getUcsName());
        }
        bi.tcvcsa02.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ucsNames));


        //For section 02
        mothersMap = new HashMap<>();
        motherCounter = 1;
        childCounter = 1;
        mothersName = new ArrayList<String>() {
            {
                add("....");
                add("Other");
            }
        };

        //For StudyID

        // ACCESSING VC FOR StudyID
        bi.tcvcsa04.setText(CheckingIDCC.accessingFile(Section01SCActivity.this, getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null)
                , MainApp.casecontrol
                , MainApp.VCID
                , ""
                , false
        ));

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
                    ClearClass.ClearAllFields(bi.llvcsa02, null);
                }
            }
        });

        bi.tcvcsa02.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    bi.tcvcsa1601.setText(null);
                    return;
                }
                bi.tcvcsa1601.setText(String.format("%02d", Integer.valueOf(ucMap.get(bi.tcvcsa02.getSelectedItem().toString()))));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

            // ACCESSING VC FOR StudyID
            CheckingIDCC.accessingFile(Section01SCActivity.this, getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null)
                    , MainApp.casecontrol
                    , MainApp.VCID
                    , ""
                    , true);

            finish();
            startActivity(new Intent(this, bi.tcvcsa09b.isChecked() || bi.tcvcsa11b.isChecked() ? EndingActivity.class : Section02SCActivity.class)
                    .putExtra("complete", true)
                    .putExtra("tcvcsa04", bi.tcvcsa04.getText().toString())
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

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
        MainApp.fc.setFormtype(MainApp.VACCINECOVERAGE);

        JSONObject f1 = new JSONObject();

        f1.put("tcvcsa01", bi.tcvcsa01.getText().toString());
        f1.put("tcvcsa02_code", ucMap.get(bi.tcvcsa02.getSelectedItem().toString()));
        f1.put("tcvcsa03", bi.tcvcsa03.getText().toString());
        f1.put("tcvcsa04", bi.tcvcsa04.getText().toString());
        f1.put("tcvcsa05", bi.tcvcsa05.getText().toString());
        f1.put("tcvcsa06", bi.tcvcsa06.getText().toString());
        f1.put("tcvcsa07", bi.tcvcsa07.getText().toString());
        f1.put("tcvcsa08", bi.tcvcsa08a.isChecked() ? "1" : bi.tcvcsa08b.isChecked() ? "2" : "0");
        f1.put("tcvcsa09", bi.tcvcsa09a.isChecked() ? "1" : bi.tcvcsa09b.isChecked() ? "2" : "0");
        f1.put("tcvcsa10", bi.tcvcsa10.getText().toString());
        f1.put("tcvcsa11", bi.tcvcsa11a.isChecked() ? "1" : bi.tcvcsa11b.isChecked() ? "2" : "0");
        f1.put("tcvcsa12", bi.tcvcsa12.getText().toString());
        f1.put("tcvcsa13", bi.tcvcsa13a.isChecked() ? "1" : bi.tcvcsa13b.isChecked() ? "2" : "0");
        f1.put("tcvcsa1496x", bi.tcvcsa1496x.getText().toString());
        f1.put("tcvcsa14", bi.tcvcsa14a.isChecked() ? "1" : bi.tcvcsa14b.isChecked() ? "2" : bi.tcvcsa14c.isChecked() ? "3" : bi.tcvcsa1496.isChecked() ? "96" : "0");
        f1.put("tcvcsa15", bi.tcvcsa15a.isChecked() ? "1" : bi.tcvcsa15b.isChecked() ? "2" : bi.tcvcsa15c.isChecked() ? "3" : bi.tcvcsa1598.isChecked() ? "98" : "0");
        f1.put("tcvcsa16", bi.tcvcsa1601.getText().toString() + bi.tcvcsa16.getText().toString());

        MainApp.fc.setsA(String.valueOf(f1));

        if (bi.tcvcsa09a.isChecked())
            childCount = Integer.valueOf(bi.tcvcsa10.getText().toString());

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
