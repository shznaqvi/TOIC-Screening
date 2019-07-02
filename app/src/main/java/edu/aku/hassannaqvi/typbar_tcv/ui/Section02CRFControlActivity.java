package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import org.json.JSONException;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection02CrfcontrolBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section02CRFControlActivity extends AppCompatActivity {

    ActivitySection02CrfcontrolBinding bi;
    DatabaseHelper db;

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
        bi.tcvsclc29.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc29a.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc30, null);
                }
            }
        });

        bi.tcvsclc22.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc2297.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc23, null);
                }
            }
        });

    }

    private void setContentUI() {
        this.setTitle(R.string.CrfControl);

        // Initialize db
        db = new DatabaseHelper(this);
    }


    public void BtnContinue() {
        try {

            if (!formValidation()) return;

            SaveDraft();

            /*if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            }*/
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

        /*JSONObject CrfControl = new JSONObject();

        CrfControl.put("tcvsclc22", bi.tcvsclc22a.isChecked() ? "1"
                : bi.tcvsclc22b.isChecked() ? "2"
                : bi.tcvsclc2297.isChecked() ? "97"
                : "0");

        CrfControl.put("tcvsclc23", bi.tcvsclc23a.isChecked() ? "1"
                : bi.tcvsclc23b.isChecked() ? "2"
                : bi.tcvsclc2396.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc2396x", bi.tcvsclc2396x.getText().toString());

        CrfControl.put("tcvsclc24", bi.tcvsclc24a.isChecked() ? "1"
                : bi.tcvsclc24b.isChecked() ? "2"
                : bi.tcvsclc2497.isChecked() ? "97"
                : "0");

        CrfControl.put("tcvsclc25", bi.tcvsclc25a.isChecked() ? "1"
                : bi.tcvsclc25b.isChecked() ? "2"
                : "0");

        CrfControl.put("tcvsclc2601", bi.tcvsclc2601a.isChecked() ? "1" : bi.tcvsclc2601b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2602", bi.tcvsclc2602a.isChecked() ? "1" : bi.tcvsclc2602b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2603", bi.tcvsclc2603a.isChecked() ? "1" : bi.tcvsclc2603b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2604", bi.tcvsclc2604a.isChecked() ? "1" : bi.tcvsclc2604b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2605", bi.tcvsclc2605a.isChecked() ? "1" : bi.tcvsclc2605b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2606", bi.tcvsclc2606a.isChecked() ? "1" : bi.tcvsclc2606b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2696", bi.tcvsclc2696a.isChecked() ? "1" : bi.tcvsclc2696b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2696x", bi.tcvsclc2696x.getText().toString());


        CrfControl.put("tcvsclc2701", bi.tcvsclc2701a.isChecked() ? "1" : bi.tcvsclc2701b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2702", bi.tcvsclc2702a.isChecked() ? "1" : bi.tcvsclc2702b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2703", bi.tcvsclc2703a.isChecked() ? "1" : bi.tcvsclc2703b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2704", bi.tcvsclc2704a.isChecked() ? "1" : bi.tcvsclc2704b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2705", bi.tcvsclc2705a.isChecked() ? "1" : bi.tcvsclc2705b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2706", bi.tcvsclc2706a.isChecked() ? "1" : bi.tcvsclc2706b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2707", bi.tcvsclc2707a.isChecked() ? "1" : bi.tcvsclc2707b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2796", bi.tcvsclc2796a.isChecked() ? "1" : bi.tcvsclc2796b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2796x", bi.tcvsclc2796x.getText().toString());


        CrfControl.put("tcvsclc2801", bi.tcvsclc2801a.isChecked() ? "1" : bi.tcvsclc2801b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2802", bi.tcvsclc2802a.isChecked() ? "1" : bi.tcvsclc2802b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2803", bi.tcvsclc2803a.isChecked() ? "1" : bi.tcvsclc2803b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2804", bi.tcvsclc2804a.isChecked() ? "1" : bi.tcvsclc2804b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2805", bi.tcvsclc2805a.isChecked() ? "1" : bi.tcvsclc2805b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2806", bi.tcvsclc2806a.isChecked() ? "1" : bi.tcvsclc2806b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2807", bi.tcvsclc2807a.isChecked() ? "1" : bi.tcvsclc2807b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2808", bi.tcvsclc2808a.isChecked() ? "1" : bi.tcvsclc2808b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2809", bi.tcvsclc2809a.isChecked() ? "1" : bi.tcvsclc2809b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2896", bi.tcvsclc2896a.isChecked() ? "1" : bi.tcvsclc2896b.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc2896x", bi.tcvsclc2896x.getText().toString());


        CrfControl.put("tcvsclc29", bi.tcvsclc29a.isChecked() ? "1"
                : bi.tcvsclc29b.isChecked() ? "2"
                : bi.tcvsclc2998.isChecked() ? "98"
                : "0");

        CrfControl.put("tcvsclc30", bi.tcvsclc30a.isChecked() ? "1"
                : bi.tcvsclc30b.isChecked() ? "2"
                : "0");

        CrfControl.put("tcvsclc31", bi.tcvsclc31.getText().toString());

        CrfControl.put("tcvsclc32", bi.tcvsclc32.getText().toString());

        try {
            JSONObject s4_merge = JsonUtils.mergeJSONObjects(new JSONObject(fc.getsA()), CrfControl);

            fc.setsA(String.valueOf(s4_merge));

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.llcrfControl);
    }

    public void BtnEnd() {
        startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false));
    }


}
