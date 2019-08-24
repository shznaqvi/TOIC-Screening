package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection01CrfControlBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class Section01CRFControlActivity extends AppCompatActivity {

    ActivitySection01CrfControlBinding bi;
    DatabaseHelper db;

    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section01_crf_control);
        bi.setCallback(this);

        setContentUI();
        setListeners();
    }

    private void setListeners() {
        bi.tcvsclc17.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc17a.isChecked())
                    bi.tcvsclc17a01.clearCheck();

            }
        });

        bi.tcvsclc19.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (!bi.tcvsclc1997.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc20, null);
                }
            }
        });

        bi.tcvsclc09.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId != bi.tcvsclc09c.getId()) {
                    ClearClass.ClearAllFields(bi.fldGrptcvsclc10, null);
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

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                startActivity(new Intent(this, Section02CRFControlActivity.class));
            }

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

//        New question added in between form
        CrfControl.put("tcvsclc01", bi.tcvsclc01.getText().toString());
        CrfControl.put("tcvsclc02", bi.tcvsclc02.getText().toString());

        CrfControl.put("tcvsclc03", bi.tcvsclc03a.isChecked() ? "1"
                : bi.tcvsclc03b.isChecked() ? "2"
                : bi.tcvsclc03c.isChecked() ? "3"
                : bi.tcvsclc03d.isChecked() ? "4"
                : "0");

        CrfControl.put("tcvsclc04", bi.tcvsclc04.getText().toString());

        CrfControl.put("tcvsclc05", bi.tcvsclc05a.isChecked() ? "1"
                : bi.tcvsclc05b.isChecked() ? "2"
                : bi.tcvsclc05c.isChecked() ? "3"
                : bi.tcvsclc0596.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc0596x", bi.tcvsclc0596x.getText().toString());

        CrfControl.put("tcvsclc06", bi.tcvsclc06a.isChecked() ? "1"
                : bi.tcvsclc06b.isChecked() ? "2"
                : bi.tcvsclc06c.isChecked() ? "3"
                : bi.tcvsclc0696.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc0696x", bi.tcvsclc0696x.getText().toString());

        CrfControl.put("tcvsclc07", bi.tcvsclc07a.isChecked() ? "1"
                : bi.tcvsclc07b.isChecked() ? "2"
                : bi.tcvsclc07c.isChecked() ? "3"
                : bi.tcvsclc0796.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc0796x", bi.tcvsclc0796x.getText().toString());

        CrfControl.put("tcvsclc0801", bi.tcvsclc0801.isChecked() ? "1" : "0");
        CrfControl.put("tcvsclc0802", bi.tcvsclc0802.isChecked() ? "2" : "0");
        CrfControl.put("tcvsclc0803", bi.tcvsclc0803.isChecked() ? "3" : "0");
        CrfControl.put("tcvsclc0804", bi.tcvsclc0804.isChecked() ? "4" : "0");
        CrfControl.put("tcvsclc0805", bi.tcvsclc0805.isChecked() ? "5" : "0");
        CrfControl.put("tcvsclc0806", bi.tcvsclc0806.isChecked() ? "6" : "0");
        CrfControl.put("tcvsclc0896", bi.tcvsclc0896.isChecked() ? "96" : "0");
        CrfControl.put("tcvsclc0896x", bi.tcvsclc0896x.getText().toString());

        CrfControl.put("tcvsclc09", bi.tcvsclc09a.isChecked() ? "1"
                : bi.tcvsclc09b.isChecked() ? "2"
                : bi.tcvsclc09c.isChecked() ? "3"
                : bi.tcvsclc09d.isChecked() ? "4"
                : bi.tcvsclc09e.isChecked() ? "5"
                : bi.tcvsclc0996.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc0996x", bi.tcvsclc0996x.getText().toString());

        CrfControl.put("tcvsclc10", bi.tcvsclc10a.isChecked() ? "1"
                : bi.tcvsclc10b.isChecked() ? "2"
                : bi.tcvsclc10c.isChecked() ? "3"
                : bi.tcvsclc10d.isChecked() ? "4"
                : "0");

        CrfControl.put("tcvsclc11", bi.tcvsclc11a.isChecked() ? "1"
                : bi.tcvsclc11b.isChecked() ? "2"
                : bi.tcvsclc11c.isChecked() ? "3"
                : "0");

        CrfControl.put("tcvsclc12", bi.tcvsclc12a.isChecked() ? "1"
                : bi.tcvsclc12b.isChecked() ? "2"
                : bi.tcvsclc12c.isChecked() ? "3"
                : bi.tcvsclc12d.isChecked() ? "4"
                : bi.tcvsclc1296.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc1296x", bi.tcvsclc1296x.getText().toString());

        CrfControl.put("tcvsclc13", bi.tcvsclc13a.isChecked() ? "1"
                : bi.tcvsclc13b.isChecked() ? "2"
                : bi.tcvsclc13c.isChecked() ? "3"
                : bi.tcvsclc1396.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc1396x", bi.tcvsclc1396x.getText().toString());

        CrfControl.put("tcvsclc14", bi.tcvsclc14a.isChecked() ? "1"
                : bi.tcvsclc14b.isChecked() ? "2"
                : bi.tcvsclc1497.isChecked() ? "97"
                : "0");

        CrfControl.put("tcvsclc15", bi.tcvsclc15a.isChecked() ? "1"
                : bi.tcvsclc15b.isChecked() ? "2"
                : bi.tcvsclc1596.isChecked() ? "96"
                : "0");
        CrfControl.put("tcvsclc1596x", bi.tcvsclc1596x.getText().toString());
        CrfControl.put("tcvsclc15ax", bi.tcvsclc15ax.getText().toString());

        CrfControl.put("tcvsclc16", bi.tcvsclc16a.isChecked() ? "1"
                : bi.tcvsclc16b.isChecked() ? "2"
                : bi.tcvsclc1697.isChecked() ? "97"
                : "0");

        CrfControl.put("tcvsclc17", bi.tcvsclc17a.isChecked() ? "1"
                : bi.tcvsclc17b.isChecked() ? "2"
                : bi.tcvsclc1798.isChecked() ? "98"
                : "0");

        CrfControl.put("tcvsclc17a01", bi.tcvsclc17a01a.isChecked() ? "1"
                : bi.tcvsclc17a01b.isChecked() ? "2"
                : bi.tcvsclc17a0198.isChecked() ? "98"
                : "0");

        CrfControl.put("tcvsclc18", bi.tcvsclc18a.isChecked() ? "1"
                : bi.tcvsclc18b.isChecked() ? "2"
                : bi.tcvsclc18c.isChecked() ? "3"
                : bi.tcvsclc18d.isChecked() ? "4"
                : "0");

        CrfControl.put("tcvsclc19", bi.tcvsclc19a.isChecked() ? "1"
                : bi.tcvsclc19b.isChecked() ? "2"
                : bi.tcvsclc1997.isChecked() ? "97"
                : "0");

        CrfControl.put("tcvsclc20", bi.tcvsclc20a.isChecked() ? "1"
                : bi.tcvsclc20b.isChecked() ? "2"
                : bi.tcvsclc2097.isChecked() ? "97"
                : "0");

        CrfControl.put("tcvsclc21", bi.tcvsclc21a.isChecked() ? "1"
                : bi.tcvsclc21b.isChecked() ? "2"
                : bi.tcvsclc21c.isChecked() ? "3"
                : bi.tcvsclc21d.isChecked() ? "4"
                : bi.tcvsclc21e.isChecked() ? "5"
                : bi.tcvsclc21f.isChecked() ? "6"
                : bi.tcvsclc2197.isChecked() ? "97"
                : "0");

        MainApp.fc.setsB(String.valueOf(CrfControl));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.llcrfControl);
    }

    public void BtnEnd() {
        startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false));
    }

}
