package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection02CrfcaseBinding;
import edu.aku.hassannaqvi.typbar_tcv.utils.JsonUtils;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

import static edu.aku.hassannaqvi.typbar_tcv.core.MainApp.fc;

public class Section02CRFCaseActivity extends AppCompatActivity {

    ActivitySection02CrfcaseBinding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bi = DataBindingUtil.setContentView(this, R.layout.activity_section02_crfcase);
        bi.setCallback(this);

        settingUI();
    }

    private void settingUI() {

        bi.tcvscad2201a2x.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, MainApp.schClasses));

        bi.tcvscad22.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == bi.tcvscad2297.getId()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvscad23, false);
                } else {
                    ClearClass.ClearAllFields(bi.fldGrptcvscad23, true);
                }
            }
        });

        bi.tcvscad29.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == bi.tcvscad29a.getId()) {
                    bi.tcvscad30.clearCheck();
                    bi.tcvscad31.setText(null);
                    bi.tcvscad32.setText(null);
                }
            }
        });

        bi.tcvscad2201.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (bi.tcvscad2201b.isChecked()) {
                    ClearClass.ClearAllFields(bi.llcrfControl01, null);
                }
            }
        });

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.crfll02);
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

    public void BtnEnd() {
        startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false));
    }

    private boolean UpdateDB() {

        DatabaseHelper db = new DatabaseHelper(this);
        long updcount = db.updateSA();
        return updcount != -1;
    }

    private void SaveDraft() throws JSONException {

        JSONObject crfCase = new JSONObject();


        crfCase.put("tcvscad2201", bi.tcvscad2201a.isChecked() ? "1"
                : bi.tcvscad2201b.isChecked() ? "2"
                : "0");
        if (bi.tcvscad2201a.isChecked()) {
            crfCase.put("tcvscad2201a1x", bi.tcvscad2201a1x.getText().toString());
            crfCase.put("tcvscad2201a2x", bi.tcvscad2201a2x.getSelectedItem().toString());
        }

        crfCase.put("tcvscad22", bi.tcvscad22a.isChecked() ? "1" : bi.tcvscad22b.isChecked() ? "2" : bi.tcvscad2297.isChecked() ? "97" : "0");
        crfCase.put("tcvscad23", bi.tcvscad23a.isChecked() ? "1" : bi.tcvscad23b.isChecked() ? "2" : bi.tcvscad2396.isChecked() ? "96" : "0");
        crfCase.put("tcvscad2396x", bi.tcvscad2396x.getText().toString());
        crfCase.put("tcvscad24", bi.tcvscad24a.isChecked() ? "1" : bi.tcvscad24b.isChecked() ? "2" : bi.tcvscad2497.isChecked() ? "97" : "0");
        crfCase.put("tcvscad25", bi.tcvscad25a.isChecked() ? "1" : bi.tcvscad25b.isChecked() ? "2" : bi.tcvscad2598.isChecked() ? "98" : "0");
        crfCase.put("tcvscad2601", bi.tcvscad2601a.isChecked() ? "1" : bi.tcvscad2601b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2602", bi.tcvscad2602a.isChecked() ? "1" : bi.tcvscad2602b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2603", bi.tcvscad2603a.isChecked() ? "1" : bi.tcvscad2603b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2604", bi.tcvscad2604a.isChecked() ? "1" : bi.tcvscad2604b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2605", bi.tcvscad2605a.isChecked() ? "1" : bi.tcvscad2605b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2606", bi.tcvscad2606a.isChecked() ? "1" : bi.tcvscad2606b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2696", bi.tcvscad2696a.isChecked() ? "1" : bi.tcvscad2696b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2696x", bi.tcvscad2696x.getText().toString());
        crfCase.put("tcvscad2701", bi.tcvscad2701a.isChecked() ? "1" : bi.tcvscad2701b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2702", bi.tcvscad2702a.isChecked() ? "1" : bi.tcvscad2702b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2703", bi.tcvscad2703a.isChecked() ? "1" : bi.tcvscad2703b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2704", bi.tcvscad2704a.isChecked() ? "1" : bi.tcvscad2704b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2705", bi.tcvscad2705a.isChecked() ? "1" : bi.tcvscad2705b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2796", bi.tcvscad2796a.isChecked() ? "1" : bi.tcvscad2796b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2796x", bi.tcvscad2796x.getText().toString());
        crfCase.put("tcvscad2801", bi.tcvscad2801a.isChecked() ? "1" : bi.tcvscad2801b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2802", bi.tcvscad2802a.isChecked() ? "1" : bi.tcvscad2802b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2803", bi.tcvscad2803a.isChecked() ? "1" : bi.tcvscad2803b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2804", bi.tcvscad2804a.isChecked() ? "1" : bi.tcvscad2804b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2805", bi.tcvscad2805a.isChecked() ? "1" : bi.tcvscad2805b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2806", bi.tcvscad2806a.isChecked() ? "1" : bi.tcvscad2806b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2807", bi.tcvscad2807a.isChecked() ? "1" : bi.tcvscad2807b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2808", bi.tcvscad2808a.isChecked() ? "1" : bi.tcvscad2808b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad2896", bi.tcvscad2896a.isChecked() ? "1" : bi.tcvscad2896b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad29", bi.tcvscad29a.isChecked() ? "1" : bi.tcvscad29b.isChecked() ? "2" : bi.tcvscad2998.isChecked() ? "98" : "0");
        crfCase.put("tcvscad30", bi.tcvscad30a.isChecked() ? "1" : bi.tcvscad30b.isChecked() ? "2" : "0");
        crfCase.put("tcvscad31", bi.tcvscad31.getText().toString());
        crfCase.put("tcvscad32", bi.tcvscad32.getText().toString());

        try {
            JSONObject s4_merge = JsonUtils.mergeJSONObjects(new JSONObject(fc.getsA()), crfCase);

            fc.setsA(String.valueOf(s4_merge));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
