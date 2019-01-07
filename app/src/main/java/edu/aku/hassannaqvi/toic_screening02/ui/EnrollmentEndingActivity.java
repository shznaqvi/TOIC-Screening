package edu.aku.hassannaqvi.toic_screening02.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import edu.aku.hassannaqvi.toic_screening02.R;
import edu.aku.hassannaqvi.toic_screening02.core.DatabaseHelper;
import edu.aku.hassannaqvi.toic_screening02.core.MainApp;
import edu.aku.hassannaqvi.toic_screening02.databinding.ActivityEnrollmentEndingBinding;
import edu.aku.hassannaqvi.toic_screening02.validation.validatorClass;

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
        } else {
            binding.istatusa.setEnabled(false);
            binding.istatusb.setEnabled(true);
        }

    }

    public void BtnEnd() {

        if (formValidation()) {
            SaveDraft();
            if (UpdateDB()) {

                finish();

                /*if (MainApp.totalChild == ChildAssessmentActivity.childCount) {
                    ChildAssessmentActivity.childCount = 1;
                    startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));
                } else {
                    ChildAssessmentActivity.childCount++;
                    startActivity(new Intent(this, ChildAssessmentActivity.class).putExtra("childFlag", true).putExtra("childRange", ChildAssessmentActivity.childCount));
                }*/

                startActivity(new Intent(this, MainActivity.class));

            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SaveDraft() {

        MainApp.ec.setIstatus(binding.istatusa.isChecked() ? "1"
                : binding.istatusb.isChecked() ? "2"
                : "0");

//        MainApp.fc.setIstatus88x(istatus88x.getText().toString());

    }

    private boolean UpdateDB() {
        DatabaseHelper db = new DatabaseHelper(this);

        int updcount = db.updateEnrollmentEnding();

        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean formValidation() {

        return validatorClass.EmptyRadioButton(this, binding.istatus, binding.istatusb, getString(R.string.istatus));
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You Can't go back", Toast.LENGTH_LONG).show();
    }


}
