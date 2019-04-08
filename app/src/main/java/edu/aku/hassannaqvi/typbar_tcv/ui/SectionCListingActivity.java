package edu.aku.hassannaqvi.typbar_tcv.ui;

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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySectionCListingBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class SectionCListingActivity extends AppCompatActivity {

    ActivitySectionCListingBinding bi;
    DatabaseHelper db;
    Map<String, SchoolContract> schoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_c_listing);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setContentUI() {
        this.setTitle(R.string.sec_clisting);

        // Initialize db
        db = new DatabaseHelper(this);
        filledSpinners();
    }

    private void filledSpinners() {
        bi.tcvcl00.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(MainApp.schTypes)));
    }

    private void setListeners() {
        bi.tcvcl11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bi.tcvcl11b.getId())
                    ClearClass.ClearAllFields(bi.childSec01);
            }
        });

        //setting dates
        String dateToday = new SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis());
        bi.tcvcl03.setManager(getSupportFragmentManager());
        bi.tcvcl03.setMaxDate(dateToday);
        bi.tcvcl19.setManager(getSupportFragmentManager());
        bi.tcvcl19.setMaxDate(dateToday);
        bi.tcvcl20.setManager(getSupportFragmentManager());

        //settting spinner listeners
        bi.tcvcl00.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<String> schNames = new ArrayList<>();
                schNames.add("....");

                if (i != 0) {

                    ArrayList<SchoolContract> schoolContract = db.getSchoolWRTType(String.valueOf(bi.tcvcl00.getSelectedItemPosition()));
                    schoolMap = new HashMap<>();

                    for (SchoolContract school : schoolContract) {
                        schoolMap.put(school.getSch_name(), school);
                        schNames.add(school.getSch_name());
                    }

                } else {
                    bi.childSec00.setVisibility(View.GONE);
                    ClearClass.ClearAllFields(bi.childSec00);
                    bi.childSec00a.setVisibility(View.GONE);
                }

                bi.tcvcl01.setAdapter(new ArrayAdapter<>(SectionCListingActivity.this, android.R.layout.simple_spinner_dropdown_item, schNames));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bi.tcvcl01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    bi.childSec00.setVisibility(View.GONE);
                    ClearClass.ClearAllFields(bi.childSec00);
                    bi.childSec00a.setVisibility(View.GONE);
                    return;
                }

                SchoolContract schoolContract = db.getSchoolWRTTypeAndCode(
                        String.valueOf(bi.tcvcl00.getSelectedItemPosition()),
                        schoolMap.get(bi.tcvcl01.getSelectedItem().toString()).getSch_code());

                if (schoolContract == null) {
                    Toast.makeText(SectionCListingActivity.this, "School not found!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!schoolContract.getSch_status().equals("1")) {
                    Toast.makeText(SectionCListingActivity.this, "School not found!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                bi.childSec00.setVisibility(View.VISIBLE);
                bi.childSec00a.setVisibility(View.VISIBLE);

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
        MainApp.fc.setFormtype("cl");

        JSONObject child = new JSONObject();
        child.put("tcvcl00", bi.tcvcl00.getSelectedItem());

        child.put("sch_code", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_code());
        child.put("sch_add", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_add());
        child.put("sch_type", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_type());
        child.put("tcvcl01", bi.tcvcl01.getSelectedItem());

        child.put("tcvcl02", bi.tcvcl02.getText().toString());
        child.put("tcvcl034", bi.tcvcl034a.isChecked() ? "DOB" : bi.tcvcl034b.isChecked() ? "AGE" : "0");
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
