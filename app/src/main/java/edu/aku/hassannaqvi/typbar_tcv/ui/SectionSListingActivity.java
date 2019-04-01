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
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySectionSListingBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class SectionSListingActivity extends AppCompatActivity {

    ActivitySectionSListingBinding bi;
    Map<String, SchoolContract> schoolMap;
    Map<String, String> ucMap;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_s_listing);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setContentUI() {
        this.setTitle(R.string.sec_slisting);

        // Initialize db
        db = new DatabaseHelper(this);
        filledSpinners();
    }

    private void filledSpinners() {
        String[] schTypes = {"....", "Government Boys Secondary School", "Government Girls Secondary School",
                "Government Boys Primary School", "Government Girls Primary School", "Private", "Madarasa", "Other"};
        bi.tcvsl00.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(schTypes)));


        ArrayList<String> ucsNames = new ArrayList<>();
        ucsNames.add("....");
        ArrayList<UCsContract> ucsContract = db.getAllUCs();
        ucMap = new HashMap<>();
        for (UCsContract uc : ucsContract) {
            ucMap.put(uc.getUcsName(), uc.getUccode());
            ucsNames.add(uc.getUcsName());
        }
        bi.tcvsl03.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ucsNames));

    }

    private void setListeners() {

        bi.tcvsl00.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<String> schNames = new ArrayList<>();
                schNames.add("....");

                if (i != 0) {

                    if (i > 5) {
                        bi.tcvsl01Name.setVisibility(View.VISIBLE);
                        bi.tcvsl01Name.setHint(bi.tcvsl00.getSelectedItem().toString() + " Name");
                        bi.tcvsl01.setVisibility(View.GONE);
                        bi.tcvsl01.setSelection(0);
                    } else {

                        ArrayList<SchoolContract> schoolContract = db.getSchoolWRTType(String.valueOf(bi.tcvsl00.getSelectedItemPosition()));
                        schoolMap = new HashMap<>();

                        for (SchoolContract school : schoolContract) {
                            schoolMap.put(school.getSch_name(), school);
                            schNames.add(school.getSch_name());
                        }

                        bi.tcvsl01.setVisibility(View.VISIBLE);
                        bi.tcvsl01Name.setVisibility(View.GONE);
                        bi.tcvsl01Name.setText(null);
                    }
                }

                bi.tcvsl01.setAdapter(new ArrayAdapter<>(SectionSListingActivity.this, android.R.layout.simple_spinner_dropdown_item, schNames));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        MainApp.fc.setFormtype("sl");

        JSONObject sA = new JSONObject();
        sA.put("tcvsl00", bi.tcvsl00.getSelectedItem());

        if (bi.tcvsl01.getVisibility() == View.VISIBLE) {
            sA.put("sch_code", schoolMap.get(bi.tcvsl01.getSelectedItem()).getSch_code());
            sA.put("sch_add", schoolMap.get(bi.tcvsl01.getSelectedItem()).getSch_add());
            sA.put("sch_type", schoolMap.get(bi.tcvsl01.getSelectedItem()).getSch_type());
            sA.put("tcvsl01", bi.tcvsl01.getSelectedItem());
        } else
            sA.put("tcvsl01Name", bi.tcvsl01Name.getText().toString());

        /*sA.put("tcvsl02", bi.tcvsl02a.isChecked() ? "1" : bi.tcvsl02b.isChecked() ? "2" :
                bi.tcvsl02c.isChecked() ? "3" : bi.tcvsl0296.isChecked() ? "96" : "0");*/

        sA.put("tcvsl03", bi.tcvsl03.getSelectedItem().toString());
        sA.put("tcvsl03_code", ucMap.get(bi.tcvsl03.getSelectedItem().toString()));
        sA.put("tcvsl04", bi.tcvsl04.getText().toString());
        sA.put("tcvsl05", bi.tcvsl05.getText().toString());
        sA.put("tcvsl06", bi.tcvsl06a.isChecked() ? "1" : bi.tcvsl06b.isChecked() ? "2" : "0");
        sA.put("tcvsl07", bi.tcvsl07.getText().toString());
        sA.put("tcvsl08", bi.tcvsl08.getText().toString());

        MainApp.fc.setsA(String.valueOf(sA));

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
