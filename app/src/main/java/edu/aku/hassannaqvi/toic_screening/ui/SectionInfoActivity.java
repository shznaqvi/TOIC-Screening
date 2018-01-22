package edu.aku.hassannaqvi.toic_screening.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.hassannaqvi.toic_screening.R;
import edu.aku.hassannaqvi.toic_screening.contracts.DistrictsContract;
import edu.aku.hassannaqvi.toic_screening.contracts.FormsContract;
import edu.aku.hassannaqvi.toic_screening.contracts.VillagesContract;
import edu.aku.hassannaqvi.toic_screening.core.DatabaseHelper;
import edu.aku.hassannaqvi.toic_screening.core.MainApp;
import edu.aku.hassannaqvi.toic_screening.databinding.ActivitySectionInfoBinding;
import edu.aku.hassannaqvi.toic_screening.validation.validatorClass;

public class SectionInfoActivity extends AppCompatActivity {

    private static final String TAG = SectionInfoActivity.class.getName();

    ActivitySectionInfoBinding binding;
    int check = 0;
    String dtToday = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date().getTime());

    DatabaseHelper db;
    Map<String, String> getAllDistricts, getAllVillages;
    List<String> Districts, Villages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_section_info);
        db = new DatabaseHelper(this);

//        Assigning data to UI binding
        binding.setCallback(this);

//        populateSpinner(); //populate spinner for ucs and villages

//        Main Working from here
//        Skip Patterns

//        Listener
        binding.toica01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.toica01.getText().toString().isEmpty()){
                    binding.fldGrp02.setVisibility(View.VISIBLE);
                    binding.fldGrp04.setVisibility(View.VISIBLE);
                }else {
                    binding.fldGrp02.setVisibility(View.GONE);
                    binding.toica02.setText(null);
                    binding.fldGrp04.setVisibility(View.GONE);
                    binding.hhno.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public Boolean formValidation() {
/*
//        Slip No
        if (!validatorClass.EmptyTextBox(this, binding.toica01, getString(R.string.toica01))) {
            return false;
        }
*/

//        Team.No
        if (binding.toica01.getText().toString().trim().isEmpty()) {
            if (!validatorClass.EmptyTextBox(this, binding.toica02, getString(R.string.toica02))) {
                return false;
            }
        }

//        HH NO
        if (!validatorClass.EmptyTextBox(this, binding.toica03, getString(R.string.toica03))) {
            return false;
        }

//        UC - HH
        if (binding.toica01.getText().toString().trim().isEmpty()) {
/*

//        UC
            if (!validatorClass.EmptySpinner(this, binding.spUCs, "UCs")) {
                return false;
            }

//        Villages
            if (!validatorClass.EmptySpinner(this, binding.spVillages, "Villages")) {
                return false;
            }
*/

//         House no
            if (!validatorClass.EmptyTextBox(this, binding.hhno, getString(R.string.hhno))) {
                return false;
            }
        }

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

//         toica09
        if (!validatorClass.EmptyTextBox(this, binding.toica09, getString(R.string.toica09))) {
            return false;
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

                startActivity(new Intent(this, EndingActivity.class).putExtra("complete", false));
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
        setGPS(); //Set GPS

        JSONObject sa = new JSONObject();

        sa.put("toica01", binding.toica01.getText().toString());
        sa.put("toica02", binding.toica02.getText().toString());
        sa.put("toica03", binding.toica03.getText().toString());

//        sa.put("ucCode", getAllDistricts.get(binding.spUCs.getSelectedItem().toString()));
//        sa.put("villageCode", getAllVillages.get(binding.spVillages.getSelectedItem().toString()));
        sa.put("hhno", binding.hhno.getText().toString());
        sa.put("toica04", binding.toica04.getText().toString());
        sa.put("toica05", binding.toica05.getText().toString());

        sa.put("toica06", binding.toica06a.isChecked() ? "1" : binding.toica06b.isChecked() ? "2" : "0");
        sa.put("toica07", binding.toica07a.isChecked() ? "1" : binding.toica07b.isChecked() ? "2" : "0");
        sa.put("toica08", binding.toica08a.isChecked() ? "1" : binding.toica08b.isChecked() ? "2" : "0");
        sa.put("toica09", binding.toica09.getText().toString());

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

            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void setGPS() {
        SharedPreferences GPSPref = getSharedPreferences("GPSCoordinates", Context.MODE_PRIVATE);
        try {
            String lat = GPSPref.getString("Latitude", "0");
            String lang = GPSPref.getString("Longitude", "0");
            String acc = GPSPref.getString("Accuracy", "0");

            if (lat == "0" && lang == "0") {
                Toast.makeText(this, "Could not obtained GPS points", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS set", Toast.LENGTH_SHORT).show();
            }

            String date = DateFormat.format("dd-MM-yyyy HH:mm", Long.parseLong(GPSPref.getString("Time", "0"))).toString();

            MainApp.fc.setGpsLat(lat);
            MainApp.fc.setGpsLng(lang);
            MainApp.fc.setGpsAcc(acc);
            MainApp.fc.setGpsDT(date); // Timestamp is converted to date above

            Toast.makeText(this, "GPS set", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "setGPS: " + e.getMessage());
        }

    }

    public void populateSpinner() {

        //        Spinner Fill

        db = new DatabaseHelper(this);

        Districts = new ArrayList<>();
        getAllDistricts = new HashMap<>();

        Districts.add("....");

        Collection<DistrictsContract> allDis = db.getAllDistricts();

        for (DistrictsContract aUCs : allDis) {
            getAllDistricts.put(aUCs.getDistrictName(), aUCs.getDistrictCode());
            Districts.add(aUCs.getDistrictName());
            Collections.sort(Districts);
        }

        binding.spUCs.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Districts));

        binding.spUCs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Villages = new ArrayList<>();
                getAllVillages = new HashMap<>();

                Villages.add("....");

                if (binding.spUCs.getSelectedItemPosition() != 0) {
                    Collection<VillagesContract> allDis = db.getAllPSUsByDistrict(getAllDistricts.get(binding.spUCs.getSelectedItem().toString()));
                    for (VillagesContract aUCs : allDis) {
                        getAllVillages.put(aUCs.getVillageName(), aUCs.getVillageCode());
                        Villages.add(aUCs.getVillageName());
                        Collections.sort(Villages);
                    }
                }

                binding.spVillages.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Villages));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
