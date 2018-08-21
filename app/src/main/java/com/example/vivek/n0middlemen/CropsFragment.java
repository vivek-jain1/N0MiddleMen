package com.example.vivek.n0middlemen;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CropsFragment extends Fragment {

    ListView cropList;
    FirebaseAuth mAuth;
    Integer imageId[] = {R.drawable.maize,R.drawable.rice,R.drawable.sugarcane,R.drawable.tobacco,R.drawable.wheat};
    FirebaseUser fbUser;
    DatabaseReference dBList,dBCrops;
    List<TwoStrings> list;
    ArrayList<TwoStrings> arrayList;
    public CropsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_crops, container, false);

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        arrayList = new ArrayList<>();
        cropList = (ListView) view.findViewById(R.id.list);
        dBList = FirebaseDatabase.getInstance().getReference(fbUser.getPhoneNumber());
        dBList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    TwoStrings strings = new TwoStrings();
                    strings = ds.getValue(TwoStrings.class);
                    arrayList.add(strings);
                    //list.add(strings);

                }
                //Toast.makeText(CropsFragment.this.getActivity(),Long.toString(dataSnapshot.getChildrenCount()),Toast.LENGTH_SHORT).show();
//
////                }
                CustomAdaptor adapter = new
                        CustomAdaptor(CropsFragment.this.getActivity(), arrayList, imageId);
                cropList.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cropList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TwoStrings string = arrayList.get(i);

                final AlertDialog.Builder builder = new AlertDialog.Builder(CropsFragment.this.getActivity());

                View view2 = LayoutInflater.from(CropsFragment.this.getActivity()).inflate(R.layout.custom_alert,null);

                TextView title = (TextView) view2.findViewById(R.id.textViewTitleAlert);
                title.setText("Do you want to remove " + string.getName() + " ?");

                dBCrops = FirebaseDatabase.getInstance().getReference(string.getName());

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dBList.child(string.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dBCrops = FirebaseDatabase.getInstance().getReference(string.getName());
                                    dBCrops.child(fbUser.getPhoneNumber()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CropsFragment.this.getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                                CropsFragment mycrops = (CropsFragment) getFragmentManager().findFragmentById(R.id.home);
                                                getFragmentManager().beginTransaction().detach(mycrops).attach(mycrops).commit();
                                            }
                                            else {
                                                dBList.child(string.getName()).setValue(string);
                                                Toast.makeText(CropsFragment.this.getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                                CropsFragment mycrops = (CropsFragment) getFragmentManager().findFragmentById(R.id.home);
                                                getFragmentManager().beginTransaction().detach(mycrops).attach(mycrops).commit();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(CropsFragment.this.getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                    CropsFragment mycrops = (CropsFragment) getFragmentManager().findFragmentById(R.id.home);
                                    getFragmentManager().beginTransaction().detach(mycrops).attach(mycrops).commit();
                                }
                            }
                        });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setView(view2);
                builder.show();
            }
        });

        return view;
    }

}
