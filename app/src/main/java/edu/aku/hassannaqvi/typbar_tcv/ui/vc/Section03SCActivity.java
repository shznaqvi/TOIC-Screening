package edu.aku.hassannaqvi.typbar_tcv.ui.vc;

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
import java.util.Date;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.MembersContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.MotherContract;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivitySection03ScBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ClearClass;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

import static edu.aku.hassannaqvi.typbar_tcv.core.MainApp.mc;
import static edu.aku.hassannaqvi.typbar_tcv.ui.vc.Section01SCActivity.mothersName;

public class Section03SCActivity extends AppCompatActivity {

    ActivitySection03ScBinding bi;
    DatabaseHelper db;
    MembersContract.FamilyTableVC mothers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section03_sc);
        bi.setCallback(this);

        db = new DatabaseHelper(this);

        setContent();
        setListeners();

    }

    private void setContent() {
        bi.tcvcsc00.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mothersName));
    }

    private void setListeners() {

        bi.tcvcsc10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (bi.tcvcsc10b.isChecked()) {
                    ClearClass.ClearAllFields(bi.fldGrpCVtcvcsc11, null);
                }
            }
        });

        bi.tcvcsc00.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                mothers = Section01SCActivity.mothersMap.get(bi.tcvcsc00.getSelectedItem().toString());
                if (!mothers.isFlag()) {
                    bi.fldGrpCVtcvcsc03.setVisibility(View.GONE);
                    bi.tcvcsc03.clearCheck();
                } else {
                    bi.fldGrpCVtcvcsc03.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void BtnContinue() {
        try {

            if (!formValidation()) return;

            SaveDraft();

            if (!UpdateDB()) {
                Toast.makeText(this, "Error in updating db!!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < mothersName.size(); i++) {
                if (mothersName.get(i).equals(bi.tcvcsc00.getSelectedItem().toString()))
                    mothersName.remove(i);
            }

            finish();
            startActivity(new Intent(this, Section04SCActivity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean UpdateDB() {

        long updcount = db.addMothersForm(mc);
        if (updcount > 0) {
            mc.set_ID(String.valueOf(updcount));
            mc.set_UID((mc.getDeviceID() + mc.get_ID()));
            db.updateMotherFormID();

            return true;
        }

        return false;
    }

    private void SaveDraft() throws JSONException {

        mc = new MotherContract();
        mc.setDevicetagID(getSharedPreferences("tagName", MODE_PRIVATE).getString("tagName", null));
        mc.setFormDate(new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime()));
        mc.setUser(MainApp.userName);
        mc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        mc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        mc.setUUID(MainApp.fc.getUID());
        mc.setFMUID(mothers.getMm().getMuid());

        JSONObject f3 = new JSONObject();

        f3.put("tcvcsc01", bi.tcvcsc01a.isChecked() ? "1" : bi.tcvcsc01b.isChecked() ? "2" : "0");
        f3.put("tcvcsc02", bi.tcvcsc02a.isChecked() ? "1" : bi.tcvcsc02b.isChecked() ? "2" : "0");
        f3.put("tcvcsc03", bi.tcvcsc03a.isChecked() ? "1" : bi.tcvcsc03b.isChecked() ? "2" : "0");
        f3.put("tcvcsc04", bi.tcvcsc04.getText().toString());
        f3.put("tcvcsc05", bi.tcvcsc05a.isChecked() ? "1" : bi.tcvcsc05b.isChecked() ? "2" : "0");
        f3.put("tcvcsc06", bi.tcvcsc06.getText().toString());
        f3.put("tcvcsc07", bi.tcvcsc07a.isChecked() ? "1" : bi.tcvcsc07b.isChecked() ? "2" : "0");
        f3.put("tcvcsc08", bi.tcvcsc08a.isChecked() ? "1" : bi.tcvcsc08b.isChecked() ? "2" : "0");
        f3.put("tcvcsc09", bi.tcvcsc09a.isChecked() ? "1" : bi.tcvcsc09b.isChecked() ? "2" : "0");
        f3.put("tcvcsc10", bi.tcvcsc10a.isChecked() ? "1" : bi.tcvcsc10b.isChecked() ? "2" : "0");
        f3.put("tcvcsc11h", bi.tcvcsc11h.getText().toString());
        f3.put("tcvcsc11", bi.tcvcsc11a.isChecked() ? "1" : bi.tcvcsc11b.isChecked() ? "2" : bi.tcvcsc11c.isChecked() ? "3" : bi.tcvcsc11d.isChecked() ? "4" : bi.tcvcsc11e.isChecked() ? "5" : bi.tcvcsc11f.isChecked() ? "6" : bi.tcvcsc11g.isChecked() ? "7" : "0");
        f3.put("tcvcsc12", bi.tcvcsc12a.isChecked() ? "1" : bi.tcvcsc12b.isChecked() ? "2" : bi.tcvcsc12c.isChecked() ? "3" : bi.tcvcsc12d.isChecked() ? "4" : bi.tcvcsc12e.isChecked() ? "5" : "0");
        f3.put("tcvcsc13", bi.tcvcsc13a.isChecked() ? "1" : bi.tcvcsc13b.isChecked() ? "2" : bi.tcvcsc13c.isChecked() ? "3" : bi.tcvcsc13d.isChecked() ? "4" : bi.tcvcsc13e.isChecked() ? "5" : "0");
        f3.put("tcvcsc14", bi.tcvcsc14a.isChecked() ? "1" : bi.tcvcsc14b.isChecked() ? "2" : bi.tcvcsc14c.isChecked() ? "3" : bi.tcvcsc14d.isChecked() ? "4" : bi.tcvcsc14e.isChecked() ? "5" : bi.tcvcsc15a.isChecked() ? "1" : bi.tcvcsc15b.isChecked() ? "2" : bi.tcvcsc15c.isChecked() ? "3" : bi.tcvcsc15d.isChecked() ? "4" : bi.tcvcsc15e.isChecked() ? "5" : "0");
        f3.put("tcvcsc16", bi.tcvcsc16a.isChecked() ? "1" : bi.tcvcsc16b.isChecked() ? "2" : bi.tcvcsc16c.isChecked() ? "3" : bi.tcvcsc16d.isChecked() ? "4" : bi.tcvcsc16e.isChecked() ? "5" : "0");
        f3.put("tcvcsc17", bi.tcvcsc17a.isChecked() ? "1" : bi.tcvcsc17b.isChecked() ? "2" : bi.tcvcsc17c.isChecked() ? "3" : bi.tcvcsc17d.isChecked() ? "4" : bi.tcvcsc17e.isChecked() ? "5" : "0");
        f3.put("tcvcsc18", bi.tcvcsc18a.isChecked() ? "1" : bi.tcvcsc18b.isChecked() ? "2" : bi.tcvcsc18c.isChecked() ? "3" : bi.tcvcsc18d.isChecked() ? "4" : bi.tcvcsc18e.isChecked() ? "5" : "0");
        f3.put("tcvcsc19", bi.tcvcsc19a.isChecked() ? "1" : bi.tcvcsc19b.isChecked() ? "2" : bi.tcvcsc19c.isChecked() ? "3" : bi.tcvcsc19d.isChecked() ? "4" : bi.tcvcsc19e.isChecked() ? "5" : "0");
        f3.put("tcvcsc20", bi.tcvcsc20a.isChecked() ? "1" : bi.tcvcsc20b.isChecked() ? "2" : bi.tcvcsc20c.isChecked() ? "3" : bi.tcvcsc20d.isChecked() ? "4" : bi.tcvcsc20e.isChecked() ? "5" : "0");
        f3.put("tcvcsc21", bi.tcvcsc21a.isChecked() ? "1" : bi.tcvcsc21b.isChecked() ? "2" : bi.tcvcsc21c.isChecked() ? "3" : bi.tcvcsc21d.isChecked() ? "4" : bi.tcvcsc21e.isChecked() ? "5" : "0");
        f3.put("tcvcsc22", bi.tcvcsc22a.isChecked() ? "1" : bi.tcvcsc22b.isChecked() ? "2" : bi.tcvcsc22c.isChecked() ? "3" : bi.tcvcsc22d.isChecked() ? "4" : bi.tcvcsc22e.isChecked() ? "5" : "0");
        f3.put("tcvcsc23", bi.tcvcsc23a.isChecked() ? "1" : bi.tcvcsc23b.isChecked() ? "2" : bi.tcvcsc23c.isChecked() ? "3" : bi.tcvcsc23d.isChecked() ? "4" : bi.tcvcsc23e.isChecked() ? "5" : "0");
        f3.put("tcvcsc24", bi.tcvcsc24a.isChecked() ? "1" : bi.tcvcsc24b.isChecked() ? "2" : bi.tcvcsc24c.isChecked() ? "3" : bi.tcvcsc24d.isChecked() ? "4" : bi.tcvcsc24e.isChecked() ? "5" : "0");
        f3.put("tcvcsc25", bi.tcvcsc25a.isChecked() ? "1" : bi.tcvcsc25b.isChecked() ? "2" : bi.tcvcsc25c.isChecked() ? "3" : bi.tcvcsc25d.isChecked() ? "4" : bi.tcvcsc25e.isChecked() ? "5" : "0");

        mc.setsC(String.valueOf(f3));

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, bi.fldGrpSecA01);
    }

    public void BtnEnd() {
        MainApp.endActivity(this, this, false);
    }

}
