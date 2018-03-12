package edu.aku.hassannaqvi.toic_screening.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.aku.hassannaqvi.toic_screening.R;
import edu.aku.hassannaqvi.toic_screening.contracts.FormsContract;
import edu.aku.hassannaqvi.toic_screening.contracts.TehsilsContract;
import edu.aku.hassannaqvi.toic_screening.contracts.UCsContract;
import edu.aku.hassannaqvi.toic_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.toic_screening.core.MainApp;
import edu.aku.hassannaqvi.toic_screening.databinding.ActivitySectionInfoBinding;
import edu.aku.hassannaqvi.toic_screening.other.IdentificationData;
import edu.aku.hassannaqvi.toic_screening.validation.validatorClass;

public class SectionInfoActivity extends Activity {

    private static final String TAG = SectionInfoActivity.class.getName();

    @BindView(R.id.toica01)
    CheckBox toica01;

    ActivitySectionInfoBinding binding;
    int check = 0;
    String dtToday = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime());

    DatabaseHelper db;
    Map<String, String> getAllTalukas, getAllUCs;
    List<String> Talukas, UCs;
    String serial;

    static String currentTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_section_info);
        db = new DatabaseHelper(this);

//        Assigning data to UI binding
        binding.setCallback(this);

        populateSpinner(); //populate spinner for ucs and villages

//        Main Working from here

//        Get Slip no for non patients
        serial = String.valueOf(Integer.valueOf(MainApp.sc.getSerialno()) + 1);

        ButterKnife.bind(this);

//        Listener
        toica01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.fldGrp04.setVisibility(View.VISIBLE);
                    binding.toica02.setText(null);
                    binding.toica02.setEnabled(true);
                } else {
                    binding.fldGrp04.setVisibility(View.GONE);
                    binding.hhteamID.setText(null);
                    binding.hhclusterID.setText(null);

                    binding.toica02.setText(serial);

                    binding.toica02.setEnabled(false);

                }
            }
        });

    }

    public Boolean formValidation() {
//        Slip No
        if (binding.toica01.isChecked()) {
            if (!validatorClass.EmptyTextBox(this, binding.toica02, getString(R.string.toica01))) {
                return false;
            }

            if (binding.toica02.getText().toString().length() != 7) {
                Toast.makeText(this, "Invalid length: " + getString(R.string.toica02), Toast.LENGTH_SHORT).show();
                binding.toica02.setError("Invalid Length");
                return false;
            } else {
                binding.toica02.setError(null);
            }
        } else {
            binding.toica02.setError(null);
        }

//        HH NO
        if (!validatorClass.EmptyTextBox(this, binding.toica03, getString(R.string.toica03))) {
            return false;
        }

//        UC - HH
//        if (!binding.toica01.isChecked()) {

//        Town
        if (!validatorClass.EmptySpinner(this, binding.spTowns, "Town")) {
            return false;
        }

//        UC
        if (!validatorClass.EmptySpinner(this, binding.spUCs, "UCs")) {
            return false;
        }

        if (binding.toica01.isChecked()) {
//         Team no
            if (!validatorClass.EmptyTextBox(this, binding.hhteamID, getString(R.string.hhteam))) {
                return false;
            }
//         Cluster no
            if (!validatorClass.EmptyTextBox(this, binding.hhclusterID, getString(R.string.hhcluster))) {
                return false;
            }
        }
//         House no
        if (!validatorClass.EmptyTextBox(this, binding.hhno, getString(R.string.hhno))) {
            return false;
        }

        if (binding.hhno.getText().toString().length() == 6) {
            String[] str = binding.hhno.getText().toString().split("-");
            if (str.length > 2 || binding.hhno.getText().toString().charAt(4) != '-' || !str[0].matches("[0-9]+") || !str[1].matches("[a-zA-Z]")) {
                binding.hhno.setError("Wrong presentation!!");
                return false;
            }
        } else {
            Toast.makeText(this, "Invalid length: " + getString(R.string.hhno), Toast.LENGTH_SHORT).show();
            binding.hhno.setError("Invalid length");
            return false;
        }

//        }

//        toica04
        if (!validatorClass.EmptyTextBox(this, binding.toica04, getString(R.string.toica04))) {
            return false;
        }
//        toica05
        if (!validatorClass.EmptyTextBox(this, binding.toica05, getString(R.string.toica05))) {
            return false;
        }
//            toica06
        if (!validatorClass.EmptyRadioButton(this, binding.toica06, binding.toica06b, getString(R.string.toica06))) {
            return false;
        }
//            toica07
        if (!validatorClass.EmptyRadioButton(this, binding.toica07, binding.toica07b, getString(R.string.toica07))) {
            return false;
        }
//            toica08
        if (!validatorClass.EmptyRadioButton(this, binding.toica08, binding.toica08b, getString(R.string.toica08))) {
            return false;
        }

        if (binding.toica06a.isChecked() && binding.toica07b.isChecked() && binding.toica08a.isChecked()) {
//         toica09
            if (!validatorClass.EmptyTextBox(this, binding.toica09, getString(R.string.toica09))) {
                return false;
            }

            if (!validatorClass.RangeTextBox(this, binding.toica09, 1, 20, "Range 1 - 20", getString(R.string.toica09))) {
                return false;
            }
        }

        return true;
    }

    public void BtnContinue() {

        Toast.makeText(this, "Processing This Section", Toast.LENGTH_SHORT).show();
        if (formValidation()) {
            try {
                SaveDraft();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (UpdateDB()) {
                Toast.makeText(this, "Starting Ending Section", Toast.LENGTH_SHORT).show();

                finish();

                startActivity(new Intent(this, ChildAssessmentActivity.class));

            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void BtnEnd() {

        Toast.makeText(this, "Processing This Section", Toast.LENGTH_SHORT).show();
        if (formValidation()) {
            try {
                SaveDraft();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (UpdateDB()) {
                Toast.makeText(this, "Starting Ending Section", Toast.LENGTH_SHORT).show();

                finish();

                startActivity(new Intent(this, EndingActivity.class).putExtra("complete", true));
            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void SaveDraft() throws JSONException {
        Toast.makeText(this, "Saving Draft for  This Section", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getSharedPreferences("tagName", MODE_PRIVATE);
        MainApp.fc = new FormsContract();


        MainApp.fc.setDevicetagID(sharedPref.getString("tagName", null));
        MainApp.fc.setFormDate(dtToday);
        MainApp.fc.setUser(MainApp.userName);
        MainApp.fc.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        MainApp.fc.setAppversion(MainApp.versionName + "." + MainApp.versionCode);
        MainApp.setGPS(FormsContract.class, this); //Set GPS

        JSONObject sa = new JSONObject();

        sa.put("teamno", MainApp.teamNo);
        sa.put("toica01", binding.toica01.isChecked() ? "1" : "2");
        sa.put("toica02", binding.toica02.getText().toString());
        sa.put("toica03", binding.toica03.getText().toString());

        sa.put("townCode", getAllTalukas.get(binding.spTowns.getSelectedItem().toString()));
        sa.put("ucCode", getAllUCs.get(binding.spUCs.getSelectedItem().toString()));
        sa.put("hhno", binding.hhno.getText().toString());

        sa.put("hhteamID", binding.hhteamID.getText().toString());
        sa.put("hhcluster", binding.hhclusterID.getText().toString());

        sa.put("toica04", binding.toica04.getText().toString());
        sa.put("toica05", binding.toica05.getText().toString());

        sa.put("toica06", binding.toica06a.isChecked() ? "1" : binding.toica06b.isChecked() ? "2" : "0");
        sa.put("toica07", binding.toica07a.isChecked() ? "1" : binding.toica07b.isChecked() ? "2" : "0");
        sa.put("toica08", binding.toica08a.isChecked() ? "1" : binding.toica08b.isChecked() ? "2" : "0");
        sa.put("toica09", binding.toica09.getText().toString());

        if (binding.toica08a.isChecked()) {

            currentTeam = binding.hhteamID.getText().toString();

            MainApp.totalChild = Integer.valueOf(binding.toica09.getText().toString());

            MainApp.identificationData = new IdentificationData(binding.toica02.getText().toString(), binding.hhteamID.getText().toString(),
                    getAllTalukas.get(binding.spTowns.getSelectedItem().toString()), getAllUCs.get(binding.spUCs.getSelectedItem().toString()),
                    binding.hhno.getText().toString());
        }

        MainApp.fc.setsA(String.valueOf(sa));


        Toast.makeText(this, "Validation Successful! - Saving Draft...", Toast.LENGTH_SHORT).show();
    }

    private boolean UpdateDB() {

        Long updcount = db.addForm(MainApp.fc);
        MainApp.fc.set_ID(String.valueOf(updcount));

        if (updcount != 0) {
            Toast.makeText(this, "Updating Database... Successful!", Toast.LENGTH_SHORT).show();

            MainApp.fc.setUID(
                    (MainApp.fc.getDeviceID() + MainApp.fc.get_ID()));
            db.updateFormID();

            if (!toica01.isChecked()) {
                if (db.updateSerialWRTDate(new SimpleDateFormat("dd-MM-yy").format(new Date()).toString(), binding.toica02.getText().toString()) != 0) {
                    Toast.makeText(this, "Updating Serial... Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Updating Serial... ERROR!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void populateSpinner() {

        //        Spinner Fill

        db = new DatabaseHelper(this);

        Talukas = new ArrayList<>();
        getAllTalukas = new HashMap<>();

        Talukas.add("....");

        Collection<TehsilsContract> allDis = db.getAllTalukas();

        for (TehsilsContract aUCs : allDis) {
            getAllTalukas.put(aUCs.getTalukaName(), aUCs.getTalukacode());
            Talukas.add(aUCs.getTalukaName());
        }

        binding.spTowns.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Talukas));

        binding.spTowns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                UCs = new ArrayList<>();
                getAllUCs = new HashMap<>();

                UCs.add("....");

                if (binding.spTowns.getSelectedItemPosition() != 0) {
                    Collection<UCsContract> allDis = db.getAllUCsByTalukas(getAllTalukas.get(binding.spTowns.getSelectedItem().toString()));
                    for (UCsContract aUCs : allDis) {
                        getAllUCs.put(aUCs.getUcsName(), aUCs.getUccode());
                        UCs.add(aUCs.getUcsName());
                    }
                }

                binding.spUCs.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, UCs));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
