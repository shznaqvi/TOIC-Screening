package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection01CrfCaseBinding;
import edu.aku.hassannaqvi.typbar_tcv.utils.DateUtils;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section01CRFCaseActivity extends AppCompatActivity {

    ActivitySection01CrfCaseBinding bi;
    DatabaseHelper db;
    Map<String, HFContract> hfMap;
    List<String> hfName = new ArrayList<>(Arrays.asList("...."));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_01_crf_case);
        bi.setCallback(this);
        EventsCall();

        setContentUI();
        loadHFFromDB();

        //setListeners();
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

    private void setContentUI() {
        this.setTitle(R.string.CrfCase);

        bi.tcvscab05.setMaxDate(DateUtils.getMonthsBack("dd/MM/yyyy", -6));


        bi.tcvscab04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().isEmpty() && !s.toString().equalsIgnoreCase("")) {

                    bi.tcvscab07.setMaxDate(s.toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bi.tcvscad09.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId != bi.tcvscad09c.getId()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvscad10, false);
                    ClearClass.ClearAllFields(bi.fldGrptcvscad11, false);

                } else {
                    ClearClass.ClearAllFields(bi.fldGrptcvscad10, true);
                    ClearClass.ClearAllFields(bi.fldGrptcvscad11, true);
                }
            }
        });


        // Initialize db
        db = new DatabaseHelper(this);
        //filledSpinners();
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
            startActivity(new Intent(this, Section02CRFCaseActivity.class));

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
        MainApp.fc.setFormtype("sca");

        JSONObject crfCase = new JSONObject();

        //crfCase.put("tcvcl00", bi.tcvcl00.getSelectedItem());

        //child.put("sch_code", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_code());
        //child.put("sch_add", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_add());
        //child.put("sch_type", schoolMap.get(bi.tcvcl01.getSelectedItem()).getSch_type());
        //child.put("tcvcl01", bi.tcvcl01.getSelectedItem());


        crfCase.put("hf_code", hfMap.get(bi.tcvmi01.getSelectedItem().toString()).getHfcode());
//        crfCase.put("tcvscaa01", bi.tcvscaa01a.isChecked() ? "1" : bi.tcvscaa01b.isChecked() ? "2" : "0");
        crfCase.put("tcvscaa02", bi.tcvscaa02a.isChecked() ? "1" : bi.tcvscaa02b.isChecked() ? "2" : "0");
        crfCase.put("tcvscaa03", bi.tcvscaa03a.isChecked() ? "1" : bi.tcvscaa03b.isChecked() ? "2" : "0");
        crfCase.put("tcvscaa04", bi.tcvscaa04a.isChecked() ? "1" : bi.tcvscaa04b.isChecked() ? "2" : "0");

        crfCase.put("tcvscab01", bi.tcvscab01.getText().toString());
        crfCase.put("tcvscab02", bi.tcvscab02.getText().toString());
        crfCase.put("tcvscab03", bi.tcvscab03.getText().toString());
        crfCase.put("tcvscab04", bi.tcvscab04.getText().toString());
        crfCase.put("tcvscaa04", bi.tcvscaa04a.isChecked() ? "1" : bi.tcvscaa04b.isChecked() ? "2" : "0");
        crfCase.put("tcvscab05DobAge", bi.tcvscab05Dob.isChecked() ? "1" : bi.tcvscab05Age.isChecked() ? "2" : "0");
        crfCase.put("tcvscab05", bi.tcvscab05.getText().toString());
        crfCase.put("tcvscab06y", bi.tcvscab06y.getText().toString());
        crfCase.put("tcvscab06m", bi.tcvscab06m.getText().toString());
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

        crfCase.put("tcvscad0801", bi.tcvscad0801.isChecked() ? "1" : "0");
        crfCase.put("tcvscad0802", bi.tcvscad0802.isChecked() ? "2" : "0");
        crfCase.put("tcvscad0803", bi.tcvscad0803.isChecked() ? "3" : "0");
        crfCase.put("tcvscad0804", bi.tcvscad0804.isChecked() ? "4" : "0");
        crfCase.put("tcvscad0805", bi.tcvscad0805.isChecked() ? "5" : "0");
        crfCase.put("tcvscad0896", bi.tcvscad0896.isChecked() ? "96" : "0");
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


        MainApp.fc.setsA(String.valueOf(crfCase));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.llcrf);
    }

    public void BtnEnd() {
        try {
            if (!ValidatorClass.EmptyCheckingContainer(this, bi.ll0104)) return;

//            if (!MainApp.checkingGPSRules(this)) return;
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

    //ClearAllFields

    void EventsCall() {

        bi.tcvscaa02.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!bi.tcvscaa02a.isChecked()) {
                    ClearClass.ClearAllFields(bi.llcrf01, null);
                }
            }
        });

        bi.tcvscaa02.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!bi.tcvscaa02a.isChecked()) {
                    ClearClass.ClearAllFields(bi.llcrf01, null);
                }
            }
        });

        bi.tcvscaa03.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!bi.tcvscaa03a.isChecked()) {
                    ClearClass.ClearAllFields(bi.llcrf01, null);
                }
            }
        });

        bi.tcvscaa04.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!bi.tcvscaa04a.isChecked()) {
                    ClearClass.ClearAllFields(bi.llcrf01, null);
                }
            }
        });

        bi.tcvscad13.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvscad13a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvscad14, null);
                }
            }
        });

        bi.tcvscad17.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvscad17a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvscad17a01, null);
                }
            }
        });



    }

}
