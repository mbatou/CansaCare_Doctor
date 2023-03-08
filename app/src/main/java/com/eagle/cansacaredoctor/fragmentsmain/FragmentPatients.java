package com.eagle.cansacaredoctor.fragmentsmain;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eagle.cansacaredoctor.R;
import com.eagle.cansacaredoctor.adapter.MyAdapter;
import com.eagle.cansacaredoctor.adapter.PatientAdapter;
import com.eagle.cansacaredoctor.ressources.Patient;
import com.eagle.cansacaredoctor.ressources.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FragmentPatients extends Fragment {
    private final List<Patient> patients = new ArrayList<>(); // Define the list of patients
    private DatabaseReference mDatabase;

    public FragmentPatients() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference("User");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_patients, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.patients_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        PatientAdapter patientAdapter = new PatientAdapter(patients); // create a new instance of PatientAdapter
        recyclerView.setAdapter(patientAdapter); // set the adapter for the RecyclerView

        // Read the posts from Firebase Realtime Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patients.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    patients.add(patient);
                }
                patientAdapter.notifyDataSetChanged(); // notify the adapter about the data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled", error.toException());
            }
        });


        return view;
    }



}
