package edu.aku.hassannaqvi.typbar_tcv.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;
import edu.aku.hassannaqvi.typbar_tcv.core.MainApp;
import edu.aku.hassannaqvi.typbar_tcv.databinding.ActivityEndingBinding;
import edu.aku.hassannaqvi.typbar_tcv.validation.ValidatorClass;

public class EndingActivity extends AppCompatActivity {

    private static final String TAG = EndingActivity.class.getSimpleName();

    ActivityEndingBinding binding;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ending);
        binding.setCallback(this);

        Boolean check = getIntent().getExtras().getBoolean("complete");

        if (check) {
            binding.istatusa.setEnabled(true);
            binding.istatusb.setEnabled(false);
        } else {
            binding.istatusa.setEnabled(false);
            binding.istatusb.setEnabled(true);
        }

/*        istatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (istatus88.isChecked()) {
                    istatus88x.setVisibility(View.VISIBLE);
                    //istatus88x.requestFocus();
                } else {
                    istatus88x.setText(null);
                    istatus88x.setVisibility(View.GONE);
                }
            }
        });*/

        if (MainApp.fc.getFormtype().equals(MainApp.VACCINECOVERAGE)) {
            flag = true;

            binding.fldGrpCVtcvcsc34.setVisibility(View.VISIBLE);
        }

    }

    public void BtnEnd() {

        if (formValidation()) {
            try {
                SaveDraft();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (UpdateDB()) {

                finish();

                Intent endSec = new Intent(this, MainActivity.class);
                startActivity(endSec);
            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SaveDraft() throws JSONException {

        MainApp.fc.setIstatus(binding.istatusa.isChecked() ? "1"
                : binding.istatusb.isChecked() ? "2"
                : "0");

        if (flag) {
            JSONObject f2 = new JSONObject();
            f2.put("tcvcsc34", binding.tcvcsc34.getText().toString());
            MainApp.fc.setsB(String.valueOf(f2));
        }
    }

    private boolean UpdateDB() {
        DatabaseHelper db = new DatabaseHelper(this);

        int updcount = db.updateEnding();

        if (flag)
            updcount = db.updateSB();

        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean formValidation() {
        return ValidatorClass.EmptyCheckingContainer(this, binding.fldGrpSecEnding);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You Can't go back", Toast.LENGTH_LONG).show();
    }


}
