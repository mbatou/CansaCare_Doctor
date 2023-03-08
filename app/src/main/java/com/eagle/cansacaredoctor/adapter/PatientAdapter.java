package com.eagle.cansacaredoctor.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eagle.cansacaredoctor.AppointmentOpened;
import com.eagle.cansacaredoctor.R;
import com.eagle.cansacaredoctor.ressources.Patient;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private final List<Patient> patients;

    public PatientAdapter(List<Patient> patients) {
        this.patients = patients;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypatients_card_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        // Get the patients at the current position
        Patient patient = patients.get(position);

        // Bind the data to the views
        holder.patientNameTextView.setText(patient.getFirstname());
        holder.patientCancerTextView.setText(patient.getTypeofcancer());

        // Set click listener for the whole item view
        holder.itemView.setOnClickListener(v -> {
            // Create intent for the new activity
            Intent intent = new Intent(v.getContext(), AppointmentOpened.class);
            intent.putExtra("firstname", patient.getFirstname());
            intent.putExtra("lastname", patient.getLastname());
            intent.putExtra("typeOfCancer", patient.getTypeofcancer());

            // Start the new activity
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder{

        TextView patientNameTextView;
        TextView patientCancerTextView;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameTextView = itemView.findViewById(R.id.patients_firstname);
            patientCancerTextView = itemView.findViewById(R.id.patients_typeofcancer);
        }
    }
}
