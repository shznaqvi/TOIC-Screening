package edu.aku.hassannaqvi.toic_screening.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;

import edu.aku.hassannaqvi.toic_screening.R;
import edu.aku.hassannaqvi.toic_screening.databinding.ActivitySecEnrollmentBinding;

public class EnrollmentActivity extends AppCompatActivity {

    ActivitySecEnrollmentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       binding = DataBindingUtil.setContentView(this,R.layout.activity_sec_enrollment);

       binding.toicc06aa.setManager(getSupportFragmentManager());

        String dateToday = new SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis());

        binding.toicc06aa.setMaxDate(dateToday);

    }


}
