package com.eagle.cansacaredoctor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eagle.cansacaredoctor.R;
import com.eagle.cansacaredoctor.fragmentsmain.FragmentPatients;
import com.eagle.cansacaredoctor.ressources.User;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;

    public MyAdapter(FragmentPatients context, ArrayList<User> userArrayList) {
        this.context = context.getContext();
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mypatients_card_patient,parent,false);

        return new MyViewHolder(v);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = userArrayList.get(position);

        holder.firstname.setText(user.getFirstname());
        holder.lastname.setText(user.getLastname());


    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //here is where you add everything you want to display
        TextView firstname, lastname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstname= itemView.findViewById(R.id.patients_firstname);
            lastname= itemView.findViewById(R.id.patients_lastname);
        }
    }
}
