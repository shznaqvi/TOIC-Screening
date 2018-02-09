package edu.aku.hassannaqvi.toic_screening.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;

import edu.aku.hassannaqvi.toic_screening.R;
import edu.aku.hassannaqvi.toic_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.toic_screening.core.MainApp;
import edu.aku.hassannaqvi.toic_screening.databinding.ActivityEndingBinding;
import edu.aku.hassannaqvi.toic_screening.databinding.ActivityEnrollmentEndingBinding;
import edu.aku.hassannaqvi.toic_screening.validation.validatorClass;

public class EnrollmentEndingActivity extends AppCompatActivity {

    private static final String TAG = EnrollmentEndingActivity.class.getSimpleName();

    ActivityEnrollmentEndingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_enrollment_ending);
        binding.setCallback(this);

        Boolean check = getIntent().getExtras().getBoolean("complete");

        if (check) {
            binding.istatusa.setEnabled(true);
            binding.istatusb.setEnabled(false);
        }else {
            binding.istatusa.setEnabled(false);
            binding.istatusb.setEnabled(true);
        }

    }

    public void BtnEnd(){

        Toast.makeText(this, "Processing This Section", Toast.LENGTH_SHORT).show();
        if (formValidation()) {
            try {
                SaveDraft();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (UpdateDB()) {

                finish();

                if (MainApp.totalChild == ChildAssessmentActivity.childCount) {
                    ChildAssessmentActivity.childCount = 1;
                    startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));
                } else {
                    ChildAssessmentActivity.childCount++;
                    startActivity(new Intent(this, ChildAssessmentActivity.class).putExtra("childFlag", true).putExtra("childRange", ChildAssessmentActivity.childCount));
                }

            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SaveDraft() throws JSONException {
        Toast.makeText(this, "Saving Draft for  This Section", Toast.LENGTH_SHORT).show();

        MainApp.cc.setIstatus(binding.istatusa.isChecked() ? "1"
                : binding.istatusb.isChecked() ? "2"
                : "0");

//        MainApp.fc.setIstatus88x(istatus88x.getText().toString());


        Toast.makeText(this, "Validation Successful! - Saving Draft...", Toast.LENGTH_SHORT).show();
    }

    private boolean UpdateDB() {
        DatabaseHelper db = new DatabaseHelper(this);

        int updcount = db.updateEnrollmentEnding();

        if (updcount == 1) {
            Toast.makeText(this, "Updating Database... Successful!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean formValidation() {
        Toast.makeText(this, "Validating This Section ", Toast.LENGTH_SHORT).show();

        if (!validatorClass.EmptyRadioButton(this, binding.istatus, binding.istatusb, getString(R.string.istatus))) {
            return false;
        }

        /*if (istatus88.isChecked()) {

            if (istatus88x.getText().toString().isEmpty()) {
                Toast.makeText(this, "ERROR(empty): " + getString(R.string.other), Toast.LENGTH_SHORT).show();
                istatus88x.setError("This data is Required!");    // Set Error on last radio button
                Log.i(TAG, "istatus88x: This data is Required!");
                return false;
            } else {
                istatus88x.setError(null);
            }

        }*/


        return true;
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You Can't go back", Toast.LENGTH_LONG).show();
    }


}
