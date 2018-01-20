package edu.aku.hassannaqvi.toic_screening.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.aku.hassannaqvi.toic_screening.R;
import edu.aku.hassannaqvi.toic_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.toic_screening.core.MainApp;

public class EndingActivity extends AppCompatActivity {

    private static final String TAG = EndingActivity.class.getSimpleName();
    @BindView(R.id.istatus)
    RadioGroup istatus;
    @BindView(R.id.istatusa)
    RadioButton istatus1;
    @BindView(R.id.istatusb)
    RadioButton istatus2;
    @BindView(R.id.istatusc)
    RadioButton istatus3;
    @BindView(R.id.istatusd)
    RadioButton istatus4;
    @BindView(R.id.istatuse)
    RadioButton istatus5;
    @BindView(R.id.istatusf)
    RadioButton istatus6;
    @BindView(R.id.istatusg)
    RadioButton istatus7;
    @BindView(R.id.istatush)
    RadioButton istatus8;
    @BindView(R.id.istatus88)
    RadioButton istatus88;
    @BindView(R.id.istatus88x)
    EditText istatus88x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);
        ButterKnife.bind(this);

        Boolean check = getIntent().getExtras().getBoolean("complete");

        if (check) {
            istatus1.setEnabled(true);
            istatus2.setEnabled(false);
            istatus3.setEnabled(false);
            istatus4.setEnabled(false);
            istatus5.setEnabled(false);
            istatus6.setEnabled(false);
            istatus7.setEnabled(false);
            istatus8.setEnabled(false);
            istatus88.setEnabled(false);
            istatus88x.setEnabled(false);
            istatus88x.setText(null);

        } else {
            //fldGrpmn0823Reason.setVisibility(View.GONE);
            //istatus1.setEnabled(false);
        }

        istatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        });

    }

    @OnClick(R.id.btn_End)
    void onBtnEndClick() {

        Toast.makeText(this, "Processing This Section", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Saving Draft for  This Section", Toast.LENGTH_SHORT).show();

        MainApp.fc.setIstatus(istatus1.isChecked() ? "1"
                : istatus2.isChecked() ? "2"
                : istatus3.isChecked() ? "3"
                : istatus4.isChecked() ? "4"
                : istatus5.isChecked() ? "4"
                : istatus6.isChecked() ? "6"
                : istatus7.isChecked() ? "7"
                : istatus8.isChecked() ? "8"
                : istatus88.isChecked() ? "88"
                : "0");

        MainApp.fc.setIstatus88x(istatus88x.getText().toString());


        Toast.makeText(this, "Validation Successful! - Saving Draft...", Toast.LENGTH_SHORT).show();
    }

    private boolean UpdateDB() {
        DatabaseHelper db = new DatabaseHelper(this);

        int updcount = db.updateEnding();
        /*if (MainApp.memFlag != 0) {
            db.updateFamilyMember();
        }
        if (MainApp.currentDeceasedCheck != 0) {
            db.updateDeceasedMother();
        }
        if (MainApp.currentMotherCheck != 0) {
            db.updateMother();
        }
        if (MainApp.totalChild != 0) {
            db.updateIM();
        }
*/
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

        if (istatus.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "ERROR(Not Selected): " + getString(R.string.dcstatus), Toast.LENGTH_LONG).show();
            istatus1.setError("Please Select One");    // Set Error on last radio button
            Log.i(TAG, "istatus: This data is Required!");
            return false;
        } else {
            istatus1.setError(null);
        }

        if (istatus88.isChecked()) {

            if (istatus88x.getText().toString().isEmpty()) {
                Toast.makeText(this, "ERROR(empty): " + getString(R.string.other), Toast.LENGTH_SHORT).show();
                istatus88x.setError("This data is Required!");    // Set Error on last radio button
                Log.i(TAG, "istatus88x: This data is Required!");
                return false;
            } else {
                istatus88x.setError(null);
            }

        }


        return true;
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You Can't go back", Toast.LENGTH_LONG).show();
    }


}
