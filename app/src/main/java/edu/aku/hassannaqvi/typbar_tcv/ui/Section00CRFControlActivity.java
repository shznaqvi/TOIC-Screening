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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.CCChildrenContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.HFContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection00CrfControlBinding;
import edu.aku.hassannaqvi.typbar_tcv.other.CheckingIDCC;
import edu.aku.hassannaqvi.typbar_tcv.utils.DateUtils;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section00CRFControlActivity extends AppCompatActivity {

    ActivitySection00CrfControlBinding bi;

    DatabaseHelper db;
    Map<String, HFContract> hfMap;
    List<String> hfName = new ArrayList<>(Arrays.asList("...."));
    private boolean eligibleFlag = false;
    private String screenID = "", controlID = "", tagID = "";
    private CCChildrenContract child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section00_crf_control);
        bi.setCallback(this);

        setContentUI();
        loadHFFromDB();
        setListeners();
    }

    public void BtnCheckCase() {
        if (!ValidatorClass.EmptyEditTextPicker(this, bi.tcvscla17, getString(R.string.tcvscla17)))
            return;

        if (child == null)
            child = db.getChildWRTCaseIDDB("T-" + bi.tcvscla17.getText().toString());

        if (child == null) {
            Toast.makeText(this, "No CaseID found!!", Toast.LENGTH_SHORT).show();
            ClearClass.ClearAllFields(bi.llcrf, null);
            bi.llcrf.setVisibility(View.GONE);
            return;
        }

        bi.viewGroup01.chName.setText(child.getTcvscaa01().toUpperCase());
        bi.viewGroup01.screenDate.setText(child.getTcvscaa08());

        bi.viewGroup01.ageNote.setText("Note: Control Child Age must be in range of: ");

        bi.llcrf.setVisibility(View.VISIBLE);

    }

    private String getDOB(CCChildrenContract child) {
        if (!child.getTcvscaa05().equals(""))
            return DateUtils.convertDateFormat(child.getTcvscaa05());
        else return DateUtils.getDOB("dd/MM/yyyy",
                Integer.valueOf(child.getTcvscaa05y()),
                Integer.valueOf(child.getTcvscaa05m()),
                Integer.valueOf(0));
    }

    private void setListeners() {

        bi.hfcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;

                if (screenID.equals("")) {
                    // ACCESSING SCREEN FOR CONTROL
                    screenID = CheckingIDCC.accessingFile(Section00CRFControlActivity.this, tagID
                            , MainApp.casecontrol
                            , MainApp.CONTROLSCR
                            , hfMap.get(bi.hfcode.getSelectedItem()).getHfcode() + "2"
                            , false
                    );

                } else {
                    String[] screenIDS = screenID.split("-");
                    screenID = screenID.replace(screenIDS[screenIDS.length - 1].substring(0, 1), hfMap.get(bi.hfcode.getSelectedItem()).getHfcode());
                }

                bi.tcvscla08.setText(screenID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        bi.hfcode.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hfNames));
    }

    private void setContentUI() {
        this.setTitle(R.string.CrfControl);

        // Initialize db
        db = new DatabaseHelper(this);
        tagID = getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null);
    }


    public void BtnContinue() {
        try {

            if (!formValidation()) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            } else {

//              INCREMENT SCREEN ID FOR CONTROL
                CheckingIDCC.accessingFile(Section00CRFControlActivity.this, tagID
                        , MainApp.casecontrol
                        , MainApp.CONTROLSCR
                        , hfMap.get(bi.hfcode.getSelectedItem()).getHfcode() + "2"
                        , true
                );

                startActivity(new Intent(this, Section01CRFControlActivity.class));
            }

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

        return true;
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
        MainApp.fc.setFormtype("scl");

        JSONObject crfControl = new JSONObject();

        crfControl.put("hf_code", hfMap.get(bi.hfcode.getSelectedItem().toString()).getHfcode());
        crfControl.put("tcvscla01", bi.tcvscla01.getText().toString());
        crfControl.put("tcvscla02", bi.tcvscla02.getText().toString());
        crfControl.put("tcvscla03", bi.tcvscla03.getText().toString());
        crfControl.put("tcvscla03a", bi.tcvscla03a.getText().toString());
        crfControl.put("tcvscla04", bi.tcvscla04.getText().toString());
        crfControl.put("tcvscla05Age", bi.tcvscla05Agea.isChecked() ? "1" : bi.tcvscla05Ageb.isChecked() ? "2" : "0");
        crfControl.put("tcvscla05", bi.tcvscla05.getText().toString());
        crfControl.put("tcvscla05y", bi.tcvscla05y.getText().toString());
        crfControl.put("tcvscla05m", bi.tcvscla05m.getText().toString());
        crfControl.put("tcvscla06", bi.tcvscla06a.isChecked() ? "1" : bi.tcvscla06b.isChecked() ? "2" : "0");
        crfControl.put("tcvscla07", bi.tcvscla07a.isChecked() ? "1" : bi.tcvscla07b.isChecked() ? "2" : "0");
        crfControl.put("tcvscla08", bi.tcvscla08.getText().toString());
        crfControl.put("tcvscla09", new SimpleDateFormat("dd-MM-yyyy").format(new Date().getTime()));

        /*New question added in between form*/

        crfControl.put("tcvscla10", bi.tcvscla10a.isChecked() ? "1" : bi.tcvscla10b.isChecked() ? "2" : "0");
        crfControl.put("tcvscla11", bi.tcvscla11a.isChecked() ? "1" : bi.tcvscla11b.isChecked() ? "2" : "0");
        crfControl.put("tcvscla12", bi.tcvscla12a.isChecked() ? "1" : bi.tcvscla12b.isChecked() ? "2" : "0");
        crfControl.put("tcvscla13", bi.tcvscla13a.isChecked() ? "1" : bi.tcvscla13b.isChecked() ? "2" : "0");
        crfControl.put("tcvscla14", bi.tcvscla14a.isChecked() ? "1" : bi.tcvscla14b.isChecked() ? "2" : "0");
        crfControl.put("tcvscla15", bi.tcvscla15a.isChecked() ? "1" : bi.tcvscla15b.isChecked() ? "2" : "0");
        crfControl.put("tcvscla16", bi.tcvscla16.getText().toString());

        eligibleFlag = bi.tcvscla10a.isChecked() && bi.tcvscla11a.isChecked() && bi.tcvscla12a.isChecked() && bi.tcvscla13a.isChecked() && bi.tcvscla14a.isChecked() && bi.tcvscla15a.isChecked();
        if (eligibleFlag) {
            crfControl.put("tcvscla17", bi.tcvscla17.getText().toString());
            crfControl.put("tcvscla18", bi.tcvscla18.getText().toString());
            crfControl.put("tcvscla19", bi.tcvscla19.getText().toString());
            crfControl.put("tcvscla20", new SimpleDateFormat("dd-MM-yyyy").format(new Date().getTime()));
            crfControl.put("tcvscla21", new SimpleDateFormat("HH:MM:SS").format(new Date().getTime()));
        }

        MainApp.fc.setsA(String.valueOf(crfControl));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.llcrfControl);
    }

    public void BtnEnd() {
        try {
            if (!ValidatorClass.EmptyEditTextPicker(this, bi.tcvscla17, getString(R.string.tcvscla17)))
                return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            } else
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

}
