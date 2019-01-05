package edu.aku.hassannaqvi.toic_screening.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.aku.hassannaqvi.toic_screening.R;
import edu.aku.hassannaqvi.toic_screening.contracts.ChildContract;
import edu.aku.hassannaqvi.toic_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.toic_screening.core.MainApp;
import edu.aku.hassannaqvi.toic_screening.databinding.ActivitySecChildassessmentBinding;
import edu.aku.hassannaqvi.toic_screening.other.childData;
import edu.aku.hassannaqvi.toic_screening.validation.validatorClass;

public class ChildAssessmentActivity extends AppCompatActivity {

    private static final String TAG = ChildAssessmentActivity.class.getSimpleName();

    public static int childCount = 1;

    ActivitySecChildassessmentBinding binding;

    String dtToday = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime());

//    int teamLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sec_childassessment);
        binding.setCallback(this);

        /*Lbl head*/
        if (getIntent().getBooleanExtra("childFlag", false)) {
            childCount = getIntent().getExtras().getInt("childRange");
        }

        binding.lblHead.setText("Child count " + String.valueOf(childCount) + " - " + String.valueOf(MainApp.totalChild));

//        teamLength = SectionInfoActivity.currentTeam.length();

//        if (teamLength > 1) {
//            binding.toicb09.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
//        } else {
//            binding.toicb09.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
//        }

    }


    public void BtnContinue() {
        if (ValidateForm()) {
            try {
                SaveDraft();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (UpdateDB()) {
                Toast.makeText(this, "Starting Next Section", Toast.LENGTH_SHORT).show();

                finish();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", new childData(binding.toicb09.getText().toString(), /*binding.toicb01.getText().toString(),
                            binding.toicb02.getText().toString(),*/ String.valueOf(childCount)));

                    startActivity(new Intent(this, EnrollmentActivity.class).putExtra("data", bundle));


            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void BtnEnd() {
        childCount = 1;
        MainApp.endActivity(this, this);
    }

    private boolean UpdateDB() {

        //Long rowId;
        DatabaseHelper db = new DatabaseHelper(this);

        Long updcount = db.addChildForm(MainApp.cc);
        MainApp.cc.set_ID(String.valueOf(updcount));

        if (updcount != 0) {
            Toast.makeText(this, "Updating Database... Successful!", Toast.LENGTH_SHORT).show();

            MainApp.cc.setUID(
                    (MainApp.cc.getDeviceID() + MainApp.cc.get_ID()));
            db.updateFormChildID();

            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void SaveDraft() throws JSONException {
        Toast.makeText(this, "Saving Draft for  This Section", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getSharedPreferences("tagName", MODE_PRIVATE);

        MainApp.cc = new ChildContract();

        MainApp.cc.setDevicetagID(sharedPref.getString("tagName", null));
        MainApp.cc.setFormDate(dtToday);
        MainApp.cc.setUser(MainApp.userName);
        MainApp.cc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        MainApp.cc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        MainApp.cc.setUUID(MainApp.fc.getUID());

        JSONObject sB = new JSONObject();

        sB.put("toicbSlipNo", MainApp.identificationData.getvSlip());
       /* sB.put("toicbTeamNo", MainApp.identificationData.getTeamNo());
        sB.put("toicbUc", MainApp.identificationData.getUc());
        sB.put("toicbTehsil", MainApp.identificationData.getTehsil());*/
        sB.put("toicbHHno", MainApp.identificationData.getHhno());


        sB.put("toicbSerial", String.valueOf(childCount));
        /*//sB.put("toicb01", binding.toicb01.getText().toString());
        sB.put("toicb02", binding.toicb02.getText().toString());
        sB.put("toicb03", binding.toicb03a.isChecked() ? "1" : binding.toicb03b.isChecked() ? "2" : "0");
        sB.put("toicb04", binding.toicb04a.isChecked() ? "1" : binding.toicb04b.isChecked() ? "2" : "0");
        sB.put("toicb05", binding.toicb05a.isChecked() ? "1" : binding.toicb05b.isChecked() ? "2" : "0");
        sB.put("toicb06", binding.toicb06a.isChecked() ? "1" : binding.toicb06b.isChecked() ? "2" : "0");
        sB.put("toicb07", binding.toicb07a.isChecked() ? "1" : binding.toicb07b.isChecked() ? "2" : "0");
        sB.put("toicb08", binding.toicb08a.isChecked() ? "1" : binding.toicb08b.isChecked() ? "2" : "0");*/
        sB.put("toicb09", binding.toicb09.getText().toString());

        MainApp.cc.setsB(String.valueOf(sB));

        Toast.makeText(this, "Validation Successful! - Saving Draft...", Toast.LENGTH_SHORT).show();

    }


    private boolean ValidateForm() {

        Toast.makeText(this, "Validating This Section ", Toast.LENGTH_SHORT).show();
/*

        if (!validatorClass.EmptyTextBox(this, binding.toicb01, getString(R.string.toicb01))) {
            return false;
        }
*/
/*
        if (!validatorClass.EmptyTextBox(this, binding.toicb02, getString(R.string.toicb02))) {
            return false;
        }*/
/*
        if (!validatorClass.EmptyRadioButton(this, binding.toicb03, binding.toicb03b, getString(R.string.toicb03))) {
            return false;
        }*/
/*
        if (!validatorClass.EmptyRadioButton(this, binding.toicb04, binding.toicb04b, getString(R.string.toicb04))) {
            return false;
        }

        if (!validatorClass.EmptyRadioButton(this, binding.toicb05, binding.toicb05b, getString(R.string.toicb05))) {
            return false;
        }

        if (!validatorClass.EmptyRadioButton(this, binding.toicb06, binding.toicb06b, getString(R.string.toicb06))) {
            return false;
        }

        if (!validatorClass.EmptyRadioButton(this, binding.toicb07, binding.toicb07b, getString(R.string.toicb07))) {
            return false;
        }

        if (!validatorClass.EmptyRadioButton(this, binding.toicb08, binding.toicb08b, getString(R.string.toicb08))) {
            return false;
        }*/

  //      if (binding.toicb03a.isChecked() /*&& binding.toicb04b.isChecked() && binding.toicb05b.isChecked()
         //       && binding.toicb06b.isChecked() && binding.toicb07b.isChecked()*/) {
            if (!validatorClass.EmptyTextBox(this, binding.toicb09, getString(R.string.toicc01))) {
                return false;
            }
           /* if (binding.toicb09.getText().toString().length() != (teamLength > 1 ? 9 : 8)) {
                Toast.makeText(this, "Invalid length: " + getString(R.string.toicc01), Toast.LENGTH_SHORT).show();
                binding.toicb09.setError("Invalid Length");
                return false;
            } else {
                binding.toicb09.setError(null);
            }*/

       // }

        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You Can't go back", Toast.LENGTH_LONG).show();
    }
}