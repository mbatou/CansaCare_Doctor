package com.eagle.cansacaredoctor.fragmentsmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.eagle.cansacaredoctor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentProfile extends Fragment {

    private FirebaseAuth mAuth;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    private void userProfile(View view) {
        TextView doctorName = view.findViewById(R.id.profile_doctor_name);
        TextView doctorTitle = view.findViewById(R.id.profile_title);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            doctorName.setText(user.getDisplayName());
            doctorTitle.setText(user.getEmail());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userProfile(view);
        return view;
    }
}
