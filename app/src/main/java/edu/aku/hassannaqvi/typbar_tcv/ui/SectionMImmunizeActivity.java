package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.HFContract;
import edu.aku.hassannaqvi.typbar_tcv.core.CheckingID;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySectionMassImunizeBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class SectionMImmunizeActivity extends AppCompatActivity {

    ActivitySectionMassImunizeBinding bi;
    DatabaseHelper db;
    Map<String, HFContract> hfMap;
    List<String> hfName = new ArrayList<>(Arrays.asList("...."));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_mass_imunize);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setContentUI() {
        this.setTitle(R.string.sec_mi);

        // Initialize db
        db = new DatabaseHelper(this);
        loadHFFromDB();
        filledSpinners(hfName);
        settingVacinnationID();
    }

    private void settingVacinnationID() {
        if (CheckingID.checkFile(SectionMImmunizeActivity.this, MainApp.massImunization)) {
            String vacID = CheckingID.accessingFile(getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null), MainApp.massImunization, false);
            bi.tcvmi18.setText(vacID);
        }
    }

    private void loadHFFromDB() {
        Collection<HFContract> allHF = db.getAllHF();
        if (allHF.size() == 0) return;
        hfName = new ArrayList<>();
        hfName.add("....");
        hfMap = new HashMap<>();
        for (HFContract hf : allHF) {
            hfName.add(hf.getHfname());
            hfMap.put(hf.getHfname(), hf);
        }
        filledSpinners(hfName);
    }

    private void filledSpinners(List<String> hfNames) {
        bi.tcvmi01.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hfNames));
    }

    private void setListeners() {
        bi.tcvmi11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bi.tcvmi11b.getId())
                    ClearClass.ClearAllFields(bi.childSec01, null);
            }
        });

        bi.tcvmi11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i != bi.tcvmi11a.getId())
                    ClearClass.ClearAllFields(bi.childSec01, null);
            }
        });

        bi.tcvmi17.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i != bi.tcvmi17.getId())
                    ClearClass.ClearAllFields(bi.childSec02, null);
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

            if (bi.tcvmi12a.isChecked() &&
                    bi.tcvmi13a.isChecked() &&
                    bi.tcvmi14a.isChecked() &&
                    bi.tcvmi15a.isChecked() &&
                    bi.tcvmi16a.isChecked()
            )
                CheckingID.accessingFile(null, MainApp.massImunization, true);

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
        MainApp.fc.setFormtype(MainApp.MASSIMMUNIZATIONTYPE);

        JSONObject child = new JSONObject();
        child.put("hf_code", hfMap.get(bi.tcvmi01.getSelectedItem().toString()).getHfcode());
        child.put("tcvmi01", bi.tcvmi01.getSelectedItem().toString());
        child.put("tcvmi02", bi.tcvmi02.getText().toString());
        child.put("tcvmi03Age", bi.tcvmi03Agea.isChecked() ? "1" : bi.tcvmi03Ageb.isChecked() ? "2" : "0");
        child.put("tcvmi03", bi.tcvmi03.getText().toString());
        child.put("tcvmi04y", bi.tcvmi04y.getText().toString());
        child.put("tcvmi04m", bi.tcvmi04m.getText().toString());
        child.put("tcvmi05", bi.tcvmi05a.isChecked() ? "1" : bi.tcvmi05b.isChecked() ? "2" : "0");
        child.put("tcvmi06", bi.tcvmi06.getText().toString());
        child.put("tcvmi07", bi.tcvmi07.getText().toString());
        child.put("tcvmi08", bi.tcvmi08.getText().toString());
        child.put("tcvmi09", bi.tcvmi09.getText().toString());
        child.put("tcvmi10", bi.tcvmi10.getText().toString());
        child.put("tcvmi11", bi.tcvmi11a.isChecked() ? "1" : bi.tcvmi11b.isChecked() ? "2" : bi.tcvmi11c.isChecked() ? "3" : "0");
        child.put("tcvmi12", bi.tcvmi12a.isChecked() ? "1" : bi.tcvmi12b.isChecked() ? "2" : "0");
        child.put("tcvmi13", bi.tcvmi13a.isChecked() ? "1" : bi.tcvmi13b.isChecked() ? "2" : "0");
        child.put("tcvmi14", bi.tcvmi14a.isChecked() ? "1" : bi.tcvmi14b.isChecked() ? "2" : "0");
        child.put("tcvmi15", bi.tcvmi15a.isChecked() ? "1" : bi.tcvmi15b.isChecked() ? "2" : "0");
        child.put("tcvmi16", bi.tcvmi16a.isChecked() ? "1" : bi.tcvmi16b.isChecked() ? "2" : "0");
        child.put("tcvmi17", bi.tcvmi17a.isChecked() ? "1" : bi.tcvmi17b.isChecked() ? "2" : "0");

        if (bi.tcvmi12a.isChecked() &&
                bi.tcvmi13a.isChecked() &&
                bi.tcvmi14a.isChecked() &&
                bi.tcvmi15a.isChecked() &&
                bi.tcvmi16a.isChecked()
        ) {
            child.put("tcvmi18", bi.tcvmi18.getText().toString());
            child.put("tcvmi19", new SimpleDateFormat("dd-MM-yyyy").format(new Date().getTime()));
            child.put("tcvmi20", new SimpleDateFormat("HH:MM:SS").format(new Date().getTime()));
        } else
            child.put("tcvmi18", "");


        MainApp.fc.setsA(String.valueOf(child));

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
