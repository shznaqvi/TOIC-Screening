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
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySectionCrfCaseBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class SectionCRFCaseActivity extends AppCompatActivity {

    ActivitySectionCrfCaseBinding bi;
    DatabaseHelper db;
    Map<String, SchoolContract> schoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_crf_case);
        bi.setCallback(this);

        setContentUI();
        //setListeners();
    }

    private void setContentUI() {
        this.setTitle(R.string.CrfCase);

        // Initialize db
        db = new DatabaseHelper(this);
        //filledSpinners();
    }

  /*
    private void filledSpinners() {
        String[] schTypes = {"....", "Government Boys Secondary School", "Government Girls Secondary School",
                "Government Boys Primary School", "Government Girls Primary School", "Private", "Madarasa", "Other"};
        bi.tcvcl00.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(schTypes)));
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

                bi.tcvcl01.setAdapter(new ArrayAdapter<>(SectionCRFCaseActivity.this, android.R.layout.simple_spinner_dropdown_item, schNames));
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
                    Toast.makeText(SectionCRFCaseActivity.this, "School not found!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!schoolContract.getSch_status().equals("1")) {
                    Toast.makeText(SectionCRFCaseActivity.this, "School not found!!", Toast.LENGTH_SHORT).show();
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

    */

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

        JSONObject crfCase = new JSONObject();

        //crfCase.put("tcvcl00", bi.tcvcl00.getSelectedItem());

        //child.put("sch_code", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_code());
        //child.put("sch_add", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_add());
        //child.put("sch_type", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_type());
        //child.put("tcvcl01", bi.tcvcl01.getSelectedItem());

        
        crfCase.put("tcvscaa01", bi.tcvscaa01a.isChecked() ? "1" : bi.tcvscaa01b.isChecked() ? "2" : "0");
        crfCase.put("tcvscaa02", bi.tcvscaa02a.isChecked() ? "1" : bi.tcvscaa02b.isChecked() ? "2" : "0");
        crfCase.put("tcvscaa03", bi.tcvscaa03a.isChecked() ? "1" : bi.tcvscaa03b.isChecked() ? "2" : "0");
        crfCase.put("tcvscaa04", bi.tcvscaa04a.isChecked() ? "1" : bi.tcvscaa04b.isChecked() ? "2" : "0");

        crfCase.put("tcvscab01", bi.tcvscab01.getText().toString());
        crfCase.put("tcvscab02", bi.tcvscab02.getText().toString());
        crfCase.put("tcvscab03", bi.tcvscab03.getText().toString());
        crfCase.put("tcvscab04", bi.tcvscab04.getText().toString());
        crfCase.put("tcvscab05", bi.tcvscab05.getText().toString());
        crfCase.put("tcvscab06", bi.tcvscab06.getText().toString());
        crfCase.put("tcvscab07", bi.tcvscab07.getText().toString());
        crfCase.put("tcvscab08", bi.tcvscab08a.isChecked() ? "1" : bi.tcvscab08b.isChecked() ? "2" : "0");

        crfCase.put("tcvscac01", bi.tcvscac01a.isChecked() ? "1" : bi.tcvscac01b.isChecked() ? "2" : "0");
        crfCase.put("tcvscac02", bi.tcvscac02a.isChecked() ? "1" : bi.tcvscac02b.isChecked() ? "2" : "0");
        crfCase.put("tcvscac03", bi.tcvscac03a.isChecked() ? "1" : bi.tcvscac03b.isChecked() ? "2" : "0");
        crfCase.put("tcvscac04", bi.tcvscac04a.isChecked() ? "1" : bi.tcvscac04b.isChecked() ? "2" : "0");
        crfCase.put("tcvscac05", bi.tcvscac05a.isChecked() ? "1" : bi.tcvscac05b.isChecked() ? "2" : "0");

        crfCase.put("tcvscad01", bi.tcvscad01.getText().toString());
        crfCase.put("tcvscad02", bi.tcvscad02.getText().toString());
        crfCase.put("tcvscad03", bi.tcvscad03a.isChecked() ? "1" : bi.tcvscad03b.isChecked() ? "2" : bi.tcvscad03c.isChecked() ? "3" : "0");
        crfCase.put("tcvscad04", bi.tcvscad04.getText().toString());
        crfCase.put("tcvscad05", bi.tcvscad05a.isChecked() ? "1" : bi.tcvscad05b.isChecked() ? "2" : bi.tcvscad05c.isChecked() ? "3" : bi.tcvscad0596.isChecked() ? "96" : "0");
        crfCase.put("tcvscad0596x", bi.tcvscad0596x.getText().toString());
        crfCase.put("tcvscad06", bi.tcvscad06a.isChecked() ? "1" : bi.tcvscad06b.isChecked() ? "2" : bi.tcvscad06c.isChecked() ? "3" : bi.tcvscad0696.isChecked() ? "96" : "0");
        crfCase.put("tcvscad0696x", bi.tcvscad0696x.getText().toString());
        crfCase.put("tcvscad07", bi.tcvscad07a.isChecked() ? "1" : bi.tcvscad07b.isChecked() ? "2" : bi.tcvscad07c.isChecked() ? "3" : bi.tcvscad0796.isChecked() ? "96" : "0");
        crfCase.put("tcvscad0796x", bi.tcvscad0796x.getText().toString());

        crfCase.put("tcvscad0801", bi.tcvscad0801.isChecked() ? "1" :  "0");
        crfCase.put("tcvscad0801", bi.tcvscad0802.isChecked() ? "2" :  "0");
        crfCase.put("tcvscad0802", bi.tcvscad0803.isChecked() ? "3" :  "0");
        crfCase.put("tcvscad0804", bi.tcvscad0804.isChecked() ? "4" :  "0");
        crfCase.put("tcvscad0805", bi.tcvscad0805.isChecked() ? "5" :  "0");
        crfCase.put("tcvscad0896", bi.tcvscad0896.isChecked() ? "96" :  "0");
        crfCase.put("tcvscad0896x", bi.tcvscad0896x.getText().toString());

        crfCase.put("tcvscad09", bi.tcvscad09a.isChecked() ? "1"
                : bi.tcvscad09b.isChecked() ? "2"
                : bi.tcvscad09c.isChecked() ? "3"
                : bi.tcvscad09d.isChecked() ? "4"
                : bi.tcvscad09e.isChecked() ? "5"
                : bi.tcvscad0996.isChecked() ? "96"
                : "0");
        crfCase.put("tcvscad0996x", bi.tcvscad0996x.getText().toString());

        crfCase.put("tcvscad10", bi.tcvscad10a.isChecked() ? "1"
                : bi.tcvscad10b.isChecked() ? "2"
                : bi.tcvscad10c.isChecked() ? "3"
                : bi.tcvscad10d.isChecked() ? "4"
                : "0");

        crfCase.put("tcvscad11", bi.tcvscad11a.isChecked() ? "1" : bi.tcvscad11b.isChecked() ? "2" : bi.tcvscad11c.isChecked() ? "3" : "0");
        crfCase.put("tcvscad12", bi.tcvscad12a.isChecked() ? "1" : bi.tcvscad12b.isChecked() ? "2" : bi.tcvscad12c.isChecked() ? "3" : bi.tcvscad12d.isChecked() ? "4" : bi.tcvscad1296.isChecked() ? "96" : "0");
        crfCase.put("tcvscad1296x", bi.tcvscad1296x.getText().toString());
        crfCase.put("tcvscad13", bi.tcvscad13a.isChecked() ? "1" : bi.tcvscad13b.isChecked() ? "2" : bi.tcvscad13c.isChecked() ? "3" : bi.tcvscad1396.isChecked() ? "96" : "0");
        crfCase.put("tcvscad1396x", bi.tcvscad1396x.getText().toString());
        crfCase.put("tcvscad14", bi.tcvscad14a.isChecked() ? "1" : bi.tcvscad14b.isChecked() ? "2" : bi.tcvscad1497.isChecked() ? "97" : "0");
        crfCase.put("tcvscad15", bi.tcvscad15a.isChecked() ? "1" : bi.tcvscad15b.isChecked() ? "2" : bi.tcvscad1596.isChecked() ? "96" : "0");
        crfCase.put("tcvscad1596x", bi.tcvscad1596x.getText().toString());
        crfCase.put("tcvscad15ax", bi.tcvscad15ax.getText().toString());
        crfCase.put("tcvscad16", bi.tcvscad16a.isChecked() ? "1" : bi.tcvscad16b.isChecked() ? "2" : bi.tcvscad1697.isChecked() ? "97" : "0");
        crfCase.put("tcvscad17", bi.tcvscad17a.isChecked() ? "1" : bi.tcvscad17b.isChecked() ? "2" : bi.tcvscad1798.isChecked() ? "98" : "0");
        crfCase.put("tcvscad17a01", bi.tcvscad17a01a.isChecked() ? "1" : bi.tcvscad17a01b.isChecked() ? "2" : bi.tcvscad17a0198.isChecked() ? "98" : "0");
        crfCase.put("tcvscad18", bi.tcvscad18a.isChecked() ? "1" : bi.tcvscad18b.isChecked() ? "2" : bi.tcvscad18c.isChecked() ? "3" : bi.tcvscad18d.isChecked() ? "4" : "0");
        crfCase.put("tcvscad19", bi.tcvscad19a.isChecked() ? "1" : bi.tcvscad19b.isChecked() ? "2" : bi.tcvscad1997.isChecked() ? "97" : "0");
        crfCase.put("tcvscad20", bi.tcvscad20a.isChecked() ? "1" : bi.tcvscad20b.isChecked() ? "2" : bi.tcvscad2097.isChecked() ? "97" : "0");
        crfCase.put("tcvscad21", bi.tcvscad21a.isChecked() ? "1" : bi.tcvscad21b.isChecked() ? "2" : bi.tcvscad21c.isChecked() ? "3" : bi.tcvscad21d.isChecked() ? "4" : bi.tcvscad21e.isChecked() ? "5" : bi.tcvscad21f.isChecked() ? "6" : bi.tcvscad2197.isChecked() ? "97" : "0");
        crfCase.put("tcvscad22", bi.tcvscad22a.isChecked() ? "1" : bi.tcvscad22b.isChecked() ? "2" : bi.tcvscad2297.isChecked() ? "97" : "0");
        crfCase.put("tcvscad23", bi.tcvscad23a.isChecked() ? "1" : bi.tcvscad23b.isChecked() ? "2" : bi.tcvscad2396.isChecked() ? "96" : "0");
        crfCase.put("tcvscad2396x", bi.tcvscad2396x.getText().toString());
        crfCase.put("tcvscad24", bi.tcvscad24a.isChecked() ? "1" : bi.tcvscad24b.isChecked() ? "2" : bi.tcvscad2497.isChecked() ? "97" : "0");
        crfCase.put("tcvscad25", bi.tcvscad25a.isChecked() ? "1" : bi.tcvscad25b.isChecked() ? "2" : "0");
        
        crfCase.put("tcvscad2601", bi.tcvscad2601.isChecked() ? "1" : "0");
        crfCase.put("tcvscad2602", bi.tcvscad2602.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2603", bi.tcvscad2603.isChecked() ? "3" : "0");
        crfCase.put("tcvscad2604", bi.tcvscad2604.isChecked() ? "4" : "0");
        crfCase.put("tcvscad2605", bi.tcvscad2605.isChecked() ? "5" : "0");
        crfCase.put("tcvscad2606", bi.tcvscad2606.isChecked() ? "6" : "0");
        crfCase.put("tcvscad2696", bi.tcvscad2696.isChecked() ? "96" : "0");
        crfCase.put("tcvscad2696x", bi.tcvscad2696x.getText().toString());
        
        crfCase.put("tcvscad2701", bi.tcvscad2701.isChecked() ? "1" : "0");
        crfCase.put("tcvscad2702", bi.tcvscad2702.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2703", bi.tcvscad2703.isChecked() ? "3" : "0");
        crfCase.put("tcvscad2704", bi.tcvscad2704.isChecked() ? "4" : "0");
        crfCase.put("tcvscad2705", bi.tcvscad2705.isChecked() ? "5" : "0");
        crfCase.put("tcvscad2796", bi.tcvscad2796.isChecked() ? "96" : "0");
        crfCase.put("tcvscad2796x", bi.tcvscad2796x.getText().toString());
        
        crfCase.put("tcvscad2801", bi.tcvscad2801.isChecked() ? "1" : "0");
        crfCase.put("tcvscad2802", bi.tcvscad2802.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2803", bi.tcvscad2803.isChecked() ? "3" : "0");
        crfCase.put("tcvscad2804", bi.tcvscad2804.isChecked() ? "4" : "0");
        crfCase.put("tcvscad2805", bi.tcvscad2805.isChecked() ? "5" : "0");
        crfCase.put("tcvscad2806", bi.tcvscad2806.isChecked() ? "6" : "0");
        crfCase.put("tcvscad2807", bi.tcvscad2807.isChecked() ? "7" : "0");
        crfCase.put("tcvscad2808", bi.tcvscad2808.isChecked() ? "8" : "0");

        crfCase.put("tcvscad29", bi.tcvscad29a.isChecked() ? "1" : bi.tcvscad29b.isChecked() ? "2" : bi.tcvscad2998.isChecked() ? "98" : "0");
        crfCase.put("tcvscad30", bi.tcvscad30a.isChecked() ? "1" : bi.tcvscad30b.isChecked() ? "2" : "0");

        crfCase.put("tcvscad31", bi.tcvscad31.getText().toString());
        crfCase.put("tcvscad32", bi.tcvscad32.getText().toString());

        MainApp.fc.setsA(String.valueOf(crfCase));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.llcrf);
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
