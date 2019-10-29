package edu.aku.hassannaqvi.typbar_tcv.ui.vc;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection04ScBinding;
import edu.aku.hassannaqvi.typbar_tcv.ui.EndingActivity;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

import static edu.aku.hassannaqvi.typbar_tcv.core.MainApp.mc;
import static edu.aku.hassannaqvi.typbar_tcv.ui.vc.Section01SCActivity.mothersName;

public class Section04SCActivity extends AppCompatActivity {

    ActivitySection04ScBinding bi;
    DatabaseHelper db;
    int checkCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section04_sc);
        bi.setCallback(this);

        bi.tcvcsc27f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    bi.fldGrpCVtcvcsc28.setVisibility(View.GONE);
                    ClearClass.ClearAllFields(bi.fldGrpLLtcvcsc27a, false);
                } else {
                    bi.fldGrpCVtcvcsc28.setVisibility(View.VISIBLE);
                    ClearClass.ClearAllFields(bi.fldGrpLLtcvcsc27a, true);
                }
            }
        });

        CheckBox.OnCheckedChangeListener check = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                    checkCounter++;
                else
                    checkCounter--;

            }
        };

        CheckBox[] checkBoxList = {bi.tcvcsc26a, bi.tcvcsc26b, bi.tcvcsc26c, bi.tcvcsc26d, bi.tcvcsc26e,
                bi.tcvcsc26f, bi.tcvcsc26g, bi.tcvcsc26h, bi.tcvcsc26i, bi.tcvcsc26j, bi.tcvcsc26k, bi.tcvcsc2696};

        for (CheckBox checkBox : checkBoxList) {
            checkBox.setOnCheckedChangeListener(check);
        }

    }

    public void BtnContinue() {
        try {

            if (!formValidation()) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            }
            finish();
            startActivity(new Intent(this, mothersName.size() > 1 ? Section03SCActivity.class : EndingActivity.class).putExtra("complete", true).putExtra("tcvcsa04", getIntent().getStringExtra("tcvcsa04")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

        db = new DatabaseHelper(this);
        long updcount = db.updateSD();
        return updcount != -1;
    }

    private void SaveDraft() throws JSONException {

        JSONObject f4 = new JSONObject();

        f4.put("tcvcsc26a", bi.tcvcsc26a.isChecked() ? "1" : "0");
        f4.put("tcvcsc26b", bi.tcvcsc26b.isChecked() ? "2" : "0");
        f4.put("tcvcsc26c", bi.tcvcsc26c.isChecked() ? "3" : "0");
        f4.put("tcvcsc26d", bi.tcvcsc26d.isChecked() ? "4" : "0");
        f4.put("tcvcsc26e", bi.tcvcsc26e.isChecked() ? "5" : "0");
        f4.put("tcvcsc26f", bi.tcvcsc26f.isChecked() ? "6" : "0");
        f4.put("tcvcsc26g", bi.tcvcsc26g.isChecked() ? "7" : "0");
        f4.put("tcvcsc26h", bi.tcvcsc26h.isChecked() ? "8" : "0");
        f4.put("tcvcsc26i", bi.tcvcsc26i.isChecked() ? "9" : "0");
        f4.put("tcvcsc26j", bi.tcvcsc26j.isChecked() ? "10" : "0");
        f4.put("tcvcsc26k", bi.tcvcsc26k.isChecked() ? "11" : "0");
        f4.put("tcvcsc26l", bi.tcvcsc26l.isChecked() ? "12" : "0");
        f4.put("tcvcsc2696", bi.tcvcsc2696.isChecked() ? "96" : "0");
        f4.put("tcvcsc2696x", bi.tcvcsc2696x.getText().toString());
        f4.put("tcvcsc27a", bi.tcvcsc27a.isChecked() ? "1" : "0");
        f4.put("tcvcsc27b", bi.tcvcsc27b.isChecked() ? "2" : "0");
        f4.put("tcvcsc27c", bi.tcvcsc27c.isChecked() ? "3" : "0");
        f4.put("tcvcsc27d", bi.tcvcsc27d.isChecked() ? "4" : "0");
        f4.put("tcvcsc27e", bi.tcvcsc27e.isChecked() ? "5" : "0");
        f4.put("tcvcsc27f", bi.tcvcsc27f.isChecked() ? "6" : "0");
        f4.put("tcvcsc28", bi.tcvcsc28a.isChecked() ? "1" : bi.tcvcsc28b.isChecked() ? "2" : bi.tcvcsc28c.isChecked() ? "3" : bi.tcvcsc28d.isChecked() ? "4" : bi.tcvcsc28e.isChecked() ? "5" : bi.tcvcsc28f.isChecked() ? "6" : bi.tcvcsc2896.isChecked() ? "96" : "0");
        f4.put("tcvcsc2896x", bi.tcvcsc2896x.getText().toString());
        f4.put("tcvcsc29", bi.tcvcsc29a.isChecked() ? "1" : bi.tcvcsc29b.isChecked() ? "2" : bi.tcvcsc29c.isChecked() ? "3" : bi.tcvcsc29d.isChecked() ? "4" : bi.tcvcsc29e.isChecked() ? "5" : bi.tcvcsc29f.isChecked() ? "6" : bi.tcvcsc2996.isChecked() ? "96" : "0");
        f4.put("tcvcsc2996x", bi.tcvcsc2996x.getText().toString());
        f4.put("tcvcsc30", bi.tcvcsc30a.isChecked() ? "1" : bi.tcvcsc30b.isChecked() ? "2" : bi.tcvcsc30c.isChecked() ? "3" : bi.tcvcsc30d.isChecked() ? "4" : bi.tcvcsc30e.isChecked() ? "5" : "0");
        f4.put("tcvcsc31", bi.tcvcsc31a.isChecked() ? "1" : bi.tcvcsc31b.isChecked() ? "2" : bi.tcvcsc31c.isChecked() ? "3" : bi.tcvcsc31d.isChecked() ? "4" : bi.tcvcsc31e.isChecked() ? "5" : "0");
        f4.put("tcvcsc32", bi.tcvcsc32a.isChecked() ? "1" : bi.tcvcsc32b.isChecked() ? "2" : bi.tcvcsc32c.isChecked() ? "3" : bi.tcvcsc32d.isChecked() ? "4" : bi.tcvcsc32e.isChecked() ? "5" : "0");
        f4.put("tcvcsc33", bi.tcvcsc33a.isChecked() ? "1" : bi.tcvcsc33b.isChecked() ? "2" : bi.tcvcsc33c.isChecked() ? "3" : bi.tcvcsc33d.isChecked() ? "4" : bi.tcvcsc33e.isChecked() ? "5" : "0");

        mc.setsD(String.valueOf(f4));

    }

    private boolean formValidation() {
        if (!ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpSecA01))
            return false;

        if (checkCounter > 3) {
            Toast.makeText(this, "ERROR(Not more then 3 Options):" + getString(R.string.tcvcsc26), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public void BtnEnd() {
        MainApp.endActivity(this, this, false);
    }

}
