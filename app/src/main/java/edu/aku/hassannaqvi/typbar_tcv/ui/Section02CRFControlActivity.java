package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection02CrfcontrolBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

import static edu.aku.hassannaqvi.typbar_tcv.core.MainApp.fc;

public class Section02CRFControlActivity extends AppCompatActivity {

    ActivitySection02CrfcontrolBinding bi;
    DatabaseHelper db;
    private Map<String, SchoolContract> schoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section02_crfcontrol);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setListeners() {

        bi.tcvsclc30.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc30a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc31, null);
                }
            }
        });
        bi.tcvsclc30.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc30a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc31, null);
                }
            }
        });

        bi.tcvsclc22.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc22a.isChecked()) {
                    ClearClass.ClearAllFields(bi.llcrfControl01, null);
                }
            }
        });

        bi.tcvsclc23.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == bi.tcvsclc2396.getId())
                    bi.tcvsclc24.clearCheck();
            }
        });

        //settting spinner listeners
        bi.tcvcl00.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<String> schNames = new ArrayList<>();

                if (i != 0) {

                    ArrayList<SchoolContract> schoolContract = db.getSchoolWRTType(String.valueOf(bi.tcvcl00.getSelectedItemPosition()), "0");
                    schoolMap = new HashMap<>();

                    for (SchoolContract school : schoolContract) {
                        schoolMap.put(school.getSch_name().toUpperCase() + "-" + school.getSch_code(), school);
                        schNames.add(school.getSch_name().toUpperCase() + "-" + school.getSch_code());
                    }

                }

                bi.tcvsclc22a1x.setText(null);
                bi.tcvsclc22a1x.setAdapter(new ArrayAdapter<>(Section02CRFControlActivity.this, android.R.layout.simple_spinner_dropdown_item, schNames));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setContentUI() {
        this.setTitle(R.string.CrfControl);

        // Initialize db
        db = new DatabaseHelper(this);

        List<String> temp_type = new ArrayList<>();

        for (String stype : MainApp.schTypes) {
            temp_type.add(stype);
        }

        bi.tcvsclc22a2x.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, MainApp.schClasses));
        bi.tcvcl00.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, temp_type));
    }


    public void BtnContinue() {
        try {

            if (!formValidation()) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            } else
                startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

        DatabaseHelper db = new DatabaseHelper(this);
        long updcount = db.updateSB();
        return updcount != -1;
    }

    private void SaveDraft() throws JSONException {

        JSONObject CrfControl = new JSONObject();

        CrfControl.put("tcvsclc22", bi.tcvsclc22a.isChecked() ? "1"
                : bi.tcvsclc22b.isChecked() ? "2"
                : "0");
        if (bi.tcvsclc22a.isChecked()) {
            if (bi.tcvcl00.getSelectedItemPosition() == 5)
                CrfControl.put("tcvsclc22a1xName", bi.tcvsclc22a1x.getText().toString());
            else
                CrfControl.put("tcvsclc22a1x", schoolMap.get(bi.tcvsclc22a1x.getText().toString()).getSch_code());
            CrfControl.put("tcvsclc22a2x", bi.tcvsclc22a2x.getSelectedItem().toString());
        }

        CrfControl.put("tcvsclc23", bi.tcvsclc23a.isChecked() ? "1"
                : bi.tcvsclc23b.isChecked() ? "2"
                : bi.tcvsclc2396.isChecked() ? "96"
                : "0");

        CrfControl.put("tcvsclc24", bi.tcvsclc24a.isChecked() ? "1"
                : bi.tcvsclc24b.isChecked() ? "2"
                : bi.tcvsclc2497.isChecked() ? "97"
                : "0");
        CrfControl.put("tcvsclc2497x", bi.tcvsclc2497x.getText().toString());

        CrfControl.put("tcvsclc25", bi.tcvsclc25a.isChecked() ? "1"
                : bi.tcvsclc25b.isChecked() ? "2"
                : "0");

        CrfControl.put("tcvsclc26", bi.tcvsclc26a.isChecked() ? "1"
                : bi.tcvsclc26b.isChecked() ? "2"
                : bi.tcvsclc2698.isChecked() ? "98"
                : "0");

        CrfControl.put("tcvsclc2701", bi.tcvsclc2701a.isChecked() ? "1" : bi.tcvsclc2701b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2702", bi.tcvsclc2702a.isChecked() ? "1" : bi.tcvsclc2702b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2703", bi.tcvsclc2703a.isChecked() ? "1" : bi.tcvsclc2703b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2704", bi.tcvsclc2704a.isChecked() ? "1" : bi.tcvsclc2704b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2705", bi.tcvsclc2705a.isChecked() ? "1" : bi.tcvsclc2705b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2706", bi.tcvsclc2706a.isChecked() ? "1" : bi.tcvsclc2706b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2796", bi.tcvsclc2796a.isChecked() ? "1" : bi.tcvsclc2796b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2796x", bi.tcvsclc2796x.getText().toString());


        CrfControl.put("tcvsclc2801", bi.tcvsclc2801a.isChecked() ? "1" : bi.tcvsclc2801b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2802", bi.tcvsclc2802a.isChecked() ? "1" : bi.tcvsclc2802b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2803", bi.tcvsclc2803a.isChecked() ? "1" : bi.tcvsclc2803b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2804", bi.tcvsclc2804a.isChecked() ? "1" : bi.tcvsclc2804b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2805", bi.tcvsclc2805a.isChecked() ? "1" : bi.tcvsclc2805b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2806", bi.tcvsclc2806a.isChecked() ? "1" : bi.tcvsclc2806b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2896", bi.tcvsclc2896a.isChecked() ? "1" : bi.tcvsclc2896b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2896x", bi.tcvsclc2896x.getText().toString());


        CrfControl.put("tcvsclc2901", bi.tcvsclc2901a.isChecked() ? "1" : bi.tcvsclc2901b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2902", bi.tcvsclc2902a.isChecked() ? "1" : bi.tcvsclc2902b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2903", bi.tcvsclc2903a.isChecked() ? "1" : bi.tcvsclc2903b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2904", bi.tcvsclc2904a.isChecked() ? "1" : bi.tcvsclc2904b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2905", bi.tcvsclc2905a.isChecked() ? "1" : bi.tcvsclc2905b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2906", bi.tcvsclc2906a.isChecked() ? "1" : bi.tcvsclc2906b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2907", bi.tcvsclc2907a.isChecked() ? "1" : bi.tcvsclc2907b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2908", bi.tcvsclc2908a.isChecked() ? "1" : bi.tcvsclc2908b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2996", bi.tcvsclc2996a.isChecked() ? "1" : bi.tcvsclc2996b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2996x", bi.tcvsclc2996x.getText().toString());


        CrfControl.put("tcvsclc30", bi.tcvsclc30a.isChecked() ? "1"
                : bi.tcvsclc30b.isChecked() ? "2"
                : bi.tcvsclc3098.isChecked() ? "98"
                : "0");

        CrfControl.put("tcvsclc31", bi.tcvsclc31a.isChecked() ? "1"
                : bi.tcvsclc31b.isChecked() ? "2"
                : "0");

        CrfControl.put("tcvsclc32", bi.tcvsclc32.getText().toString());

        CrfControl.put("tcvsclc33", bi.tcvsclc33.getText().toString());

        fc.setsB(String.valueOf(CrfControl));

    }

    private boolean formValidation() {
        if (!ValidatorClass.EmptyCheckingContainer(this, bi.llcrfControl))
            return false;

        if (bi.tcvsclc22a1x.getVisibility() == View.VISIBLE) {

            if (bi.tcvcl00.getSelectedItemPosition() == 5) return true;

            if (schoolMap.get(bi.tcvsclc22a1x.getText().toString()) != null) return true;

            return ValidatorClass.EmptyCustomTextBox(this, bi.tcvsclc22a1x, "This data is not accurate!!");

        }

        return true;
    }

    public void BtnEnd() {
        startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false));
    }


}
