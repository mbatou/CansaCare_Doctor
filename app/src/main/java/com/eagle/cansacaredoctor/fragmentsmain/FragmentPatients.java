package com.eagle.cansacaredoctor.fragmentsmain;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eagle.cansacaredoctor.R;
import com.eagle.cansacaredoctor.adapter.MyAdapter;
import com.eagle.cansacaredoctor.ressources.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FragmentPatients extends Fragment {

    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    public FragmentPatients() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patients, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();


        recyclerView = v.findViewById(R.id.patients_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<User>();
        myAdapter = new MyAdapter(FragmentPatients.this, userArrayList);

        EventChangeListener();

        return v;

    }

    @SuppressLint("NotifyDataSetChanged")
    private void EventChangeListener() {

        db.collection("User")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Log.e(TAG, "EventChangeListener: Listen failed.", error);
                        return;
                    }

                    assert value != null;
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            userArrayList.add(dc.getDocument().toObject(User.class));
                        }
                    }

                    myAdapter.notifyDataSetChanged();
                });
    }

}
