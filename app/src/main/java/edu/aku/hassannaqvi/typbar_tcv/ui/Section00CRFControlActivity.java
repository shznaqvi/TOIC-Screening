package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

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
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract.FormsTableCaseControlForm;
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
    List<RadioButton> rdbEligibilityCheckIDs, rdbEligibilityCheckIDs07a, rdbEligibilityCheckIDs07b;

    private int minMonth, maxMonth;
    private boolean flag;

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
            child = db.getChildWRTCaseControlIDDB(MainApp.CRFCase, "T-" + bi.tcvscla17.getText().toString());

        if (child == null) {
            notFoundCase();
            return;
        }

        if (!db.getChildWRTCaseIDDB02(child.getLuid(), child.getTcvscaa07(), child.getTcvscaa08(), child.getTcvscab23())) {
            notFoundCase();
            return;
        }

        Long ageInMonth = DateUtils.ageInMonthsByDOB(getDOB(child));

        if (!getAgeRange(ageInMonth)) {
            notFoundCase();
            return;
        }

        List<FormsContract> childLst = db.getChildWRTCaseIDInControl(child.getLuid(), child.getTcvscaa07(), child.getTcvscaa08(), child.getTcvscab23());

        int hospitalCount = 0, communityCount = 0;

        for (FormsContract form : childLst) {

            FormsTableCaseControlForm singleForm = new Gson().fromJson(form.getsA(), FormsTableCaseControlForm.class);

            if (singleForm.getTcvscla07().equals("1"))
                communityCount++;
            else
                hospitalCount++;
        }

        if (hospitalCount > 0)
            bi.tcvscla07b.setEnabled(false);

        if (childLst.size() > 0) {
            FormsTableCaseControlForm lastForm = new Gson().fromJson(childLst.get(childLst.size() - 1).getsA(), FormsTableCaseControlForm.class);
            String controlNo = lastForm.getTcvscla19().substring(lastForm.getTcvscla19().length() - 1);
            settingControlIDs(String.valueOf(Integer.valueOf(controlNo) + 1));
        } else
            settingControlIDs("1");

        bi.viewGroup01.hCount.setText(String.valueOf(hospitalCount));
        bi.viewGroup01.cCount.setText(String.valueOf(communityCount));
        bi.viewGroup01.chName.setText(child.getTcvscaa01().toUpperCase());
        bi.viewGroup01.chDOB.setText(getDOB(child));

        if (childLst.size() == 3) {
            bi.llsec01.setVisibility(View.VISIBLE);
            bi.llcrf.setVisibility(View.GONE);
            bi.viewGroup01.ageNote.setText("Note: All Control's are filled!!");
            return;
        }

        bi.viewGroup01.ageNote.setText("Note: Case Child Age is " + convertMonthsToAge(ageInMonth.intValue() - 1) + "\nControl Child range must be in " + convertMonthsToAge(minMonth - 1) + " to " + convertMonthsToAge(maxMonth - 1));
        bi.llcrf.setVisibility(View.VISIBLE);
        bi.llsec01.setVisibility(View.VISIBLE);

    }

    private String convertMonthsToAge(int months) {
        int year = months / 12;
        int month = months % 12;

        return year + " Year & " + month + " Months";
    }

    private void settingControlIDs(String controlNo) {
        bi.tcvscla18.setText(controlNo);
        bi.tcvscla19.setText("T-" + bi.tcvscla17.getText().toString() + controlNo);
    }

    private void notFoundCase() {
        Toast.makeText(this, "No CaseID found!!", Toast.LENGTH_SHORT).show();
        ClearClass.ClearAllFields(bi.llcrf, null);
        bi.llcrf.setVisibility(View.GONE);
    }

    private boolean getAgeRange(Long months) {

        //Age must be 7Months to 15Years
        if (months < 7 && months >= 192) return false;

        if (months >= 6 && months < 36) {
            int minMonthCalculate = months.intValue() - 6;
            minMonth = minMonthCalculate < 7 ? 7 : minMonthCalculate;
            int maxMonthCalculate = months.intValue() + 6;
            maxMonth = maxMonthCalculate > 36 ? 36 : maxMonthCalculate;
        }

        if (months >= 36 && months < 192) {
            int minMonthCalculate = months.intValue() - 36;
            minMonth = minMonthCalculate < 7 ? 7 : minMonthCalculate;
            int maxMonthCalculate = months.intValue() + 36;
            maxMonth = maxMonthCalculate > 192 ? 192 : maxMonthCalculate;
        }

        return true;
    }

    private String getDOB(CCChildrenContract child) {
        if (!child.getTcvscaa05().equals(""))
            return child.getTcvscaa05();
        else return DateUtils.getDOB("dd-MM-yyyy",
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

        bi.tcvscla07.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bi.tcvscla07a.getId()) {
                    bi.tcvscla12.clearCheck();
                } else {
                    bi.tcvscla11.clearCheck();
                    bi.tcvscla13.clearCheck();
                }
            }
        });

    }

    public void onRadioClickChanged(RadioGroup radioGroup, int id) {
        flag = true;
        for (RadioButton rdbID : rdbEligibilityCheckIDs) {
            if (!rdbID.isChecked()) {
                flag = false;
                break;
            }
        }
        if (bi.tcvscla07a.isChecked()) {
            for (RadioButton rdbID : rdbEligibilityCheckIDs07a) {
                if (!rdbID.isChecked()) {
                    flag = false;
                    break;
                }
            }
        } else if (bi.tcvscla07b.isChecked()) {
            for (RadioButton rdbID : rdbEligibilityCheckIDs07b) {
                if (!rdbID.isChecked()) {
                    flag = false;
                    break;
                }
            }
        }

        bi.fldGrp0130.setVisibility(flag ? View.GONE : View.VISIBLE);
        bi.fldGrp0131.setVisibility(flag ? View.VISIBLE : View.GONE);


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

        rdbEligibilityCheckIDs = new ArrayList<>(Arrays.asList(bi.tcvscla10a, bi.tcvscla14a, bi.tcvscla15a));
        rdbEligibilityCheckIDs07a = new ArrayList<>(Arrays.asList(bi.tcvscla07a, bi.tcvscla11a, bi.tcvscla13a));
        rdbEligibilityCheckIDs07b = new ArrayList<>(Arrays.asList(bi.tcvscla07b, bi.tcvscla12a));
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

                startActivity(new Intent(this, EndingActivity.class)
                        .putExtra("complete", true));
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
        MainApp.fc.setFormtype(MainApp.CRFControl);

        JSONObject crfControl = new JSONObject();

        crfControl.put("ch_name", child.getTcvscaa01().toUpperCase());
        crfControl.put("ch_screenid", child.getTcvscaa07());
        crfControl.put("ch_dob", getDOB(child));
        crfControl.put("ch_screendt", child.getTcvscaa08());
        crfControl.put("ch_formdt", child.getLformdate());
        crfControl.put("ch_luid", child.getLuid());

        crfControl.put("tcvscla17", "T-" + bi.tcvscla17.getText().toString());
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

        crfControl.put("tcvscla18", "");

        eligibleFlag = bi.tcvscla10a.isChecked() && (!bi.tcvscla07a.isChecked() || bi.tcvscla11a.isChecked()) && (!bi.tcvscla07b.isChecked() || bi.tcvscla12a.isChecked()) && (!bi.tcvscla07a.isChecked() || bi.tcvscla13a.isChecked()) && bi.tcvscla14a.isChecked() && bi.tcvscla15a.isChecked();
        if (eligibleFlag) {
            crfControl.put("tcvscla18", bi.tcvscla18.getText().toString());
            crfControl.put("tcvscla19", bi.tcvscla19.getText().toString());
            crfControl.put("tcvscla20", new SimpleDateFormat("dd-MM-yyyy").format(new Date().getTime()));
            crfControl.put("tcvscla21", new SimpleDateFormat("HH:MM:SS").format(new Date().getTime()));
        }

        MainApp.fc.setsA(String.valueOf(crfControl));

    }

    private boolean formValidation() {
        if (!ValidatorClass.EmptyCheckingContainer(this, bi.llcrfControl))
            return false;

        String currentDOB = getDOB(new CCChildrenContract(bi.tcvscla05.getText().toString(), bi.tcvscla05y.getText().toString(), bi.tcvscla05m.getText().toString()));
        Long currentAgeInMonth = DateUtils.ageInMonthsByDOB(currentDOB);

        if (currentAgeInMonth < minMonth || currentAgeInMonth > maxMonth) {
            Toast.makeText(this, "Age in not coming in range!!", Toast.LENGTH_LONG).show();

            return false;
        }

        if (bi.tcvscla03.getText().toString().length() > 0)
            return ValidatorClass.EmptyEditTextPicker(this, bi.tcvscla03, getString(R.string.tcvscla03));

        if (bi.tcvscla03a.getText().toString().length() > 0)
            return ValidatorClass.EmptyEditTextPicker(this, bi.tcvscla03a, getString(R.string.tcvscla03a));

        if (bi.tcvscla05Ageb.isChecked()) {
            if (Integer.valueOf(bi.tcvscla05y.getText().toString()) == 0 && Integer.valueOf(bi.tcvscla05m.getText().toString()) < 7) {
                Toast.makeText(this, "Both Year and Month criteria not meet!!", Toast.LENGTH_SHORT).show();
                bi.tcvscla05m.setError("Both Year and Month criteria not meet!!");
                return false;
            }
        }

        return true;
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
