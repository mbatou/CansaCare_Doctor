package com.eagle.cansacaredoctor.fragmentsmain;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eagle.cansacaredoctor.R;
import com.eagle.cansacaredoctor.adapter.AppointmentAdapter;
import com.eagle.cansacaredoctor.ressources.Appointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentAppointments extends Fragment {

    private final List<Appointment> appointments = new ArrayList<>(); // Define the list of appointments

    DatabaseReference mDatabase;

    public FragmentAppointments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference("appointments");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.appointments_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(appointments); // create a new instance of AppointmentAdapter
        recyclerView.setAdapter(appointmentAdapter); // set the adapter for the RecyclerView

        // Read the appointments from Firebase Realtime Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    appointments.add(appointment);
                }
                appointmentAdapter.notifyDataSetChanged(); // notify the adapter about the data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled", error.toException());
            }
        });

        return view;
    }
}
