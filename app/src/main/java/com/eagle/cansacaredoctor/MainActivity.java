package com.eagle.cansacaredoctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.eagle.cansacaredoctor.fragmentsmain.FragmentAppointments;
import com.eagle.cansacaredoctor.fragmentsmain.FragmentPatients;
import com.eagle.cansacaredoctor.fragmentsmain.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        performFragmentTransaction(new FragmentAppointments(), "Feed");

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int menuSelectedId = item.getItemId();

            Fragment fragment;

            if (menuSelectedId == R.id.main_activity_appointments) {
                fragment = new FragmentAppointments();
            } else if (menuSelectedId == R.id.main_activity_patients) {
                fragment = new FragmentPatients();
            } else {
                fragment = new FragmentProfile();
            }
            performFragmentTransaction(fragment, "");
            return true;
        });

    }

    private void performFragmentTransaction(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_frame_layout, fragment, tag)
                .commit();

    }

}