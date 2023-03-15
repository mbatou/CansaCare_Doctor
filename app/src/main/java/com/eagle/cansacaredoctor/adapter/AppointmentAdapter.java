package com.eagle.cansacaredoctor.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eagle.cansacaredoctor.AppointmentOpened;
import com.eagle.cansacaredoctor.R;
import com.eagle.cansacaredoctor.ressources.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>{

    private final List<Appointment> appointments;
    private final CollectionReference databaseReferenceStore = FirebaseFirestore.getInstance().collection("User");
    String appointmentPatientName;


    public AppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_card, parent, false);
        return new AppointmentAdapter.AppointmentViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {

        // Get the post at the current position
        Appointment appointment = appointments.get(position);

            // Bind the data to the views
        holder.appointmentTimeView.setText(appointment.getTime());
        holder.appointmentDate.setText(appointment.getDate());
//        holder.appointmentType.setText(appointment.getType());
        holder.appointmentPatientName.setText(appointment.getPatientName());


        // Set click listener for the whole item view
        holder.itemView.setOnClickListener(v -> {
            // Create intent for the new activity
            Intent intent = new Intent(v.getContext(), AppointmentOpened.class);
//            intent.putExtra("content", notification.getContent());
//            intent.putExtra("author", notification.getAuthorId());
//            intent.putExtra("time", notification.getPostTime());
//            intent.putExtra("postId", notification.getId());

            // Start the new activity
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder{

        TextView appointmentTimeView, appointmentDate;
        TextView appointmentPatientName;



        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            appointmentPatientName =itemView.findViewById(R.id.appointment_card_patient_name);
            appointmentTimeView =itemView.findViewById(R.id.appointment_time);
            appointmentDate =itemView.findViewById(R.id.appointment_date);
        }
    }
}
