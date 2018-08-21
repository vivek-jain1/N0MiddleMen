package com.example.vivek.n0middlemen;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SellFragment extends Fragment {

    Spinner spinner2;
    Button buttonAdd,buttonAgain;
    FirebaseAuth mAuth;
    DatabaseReference dBUsers,dBCrops;
    DatabaseReference dBMyCrops;
    FirebaseUser fbUser;
    CardView card;
    EditText price;
    TextView t1;


    private List<Crop> cropdetails;

    public SellFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 = inflater.inflate(R.layout.fragment_sell, container, false);

        mAuth = FirebaseAuth.getInstance();

        dBUsers = FirebaseDatabase.getInstance().getReference("Users");

        fbUser = mAuth.getCurrentUser();
        buttonAdd = (Button) view1.findViewById(R.id.buttonAdd);
        buttonAgain = (Button) view1.findViewById(R.id.buttonAgain);
        spinner2 = (Spinner) view1.findViewById(R.id.spinner2);
        t1 = (TextView) view1.findViewById(R.id.t1);
        card = (CardView) view1.findViewById(R.id.card);
        price = (EditText) view1.findViewById(R.id.price) ;
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.crops, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner2.setAdapter(adapter1);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dBUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final TwoStrings string = new TwoStrings(spinner2.getSelectedItem().toString(),price.getText().toString());
                        User user = new User();
                        user = dataSnapshot.child(fbUser.getPhoneNumber()).getValue(User.class);

                        Crop crop = new Crop(user.getDistrict(),fbUser.getPhoneNumber(),user.getName(),price.getText().toString());

                        dBCrops = FirebaseDatabase.getInstance().getReference(spinner2.getSelectedItem().toString());
                        dBCrops.child(fbUser.getPhoneNumber()).setValue(crop).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    dBMyCrops = FirebaseDatabase.getInstance().getReference(fbUser.getPhoneNumber());
                                    dBMyCrops.child(spinner2.getSelectedItem().toString()).setValue(string).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                t1.setText("Information saved");
                                                price.setText("");
                                                price.clearFocus();
                                                price.setEnabled(false);
                                                spinner2.setEnabled(false);
                                                buttonAdd.setVisibility(GONE);
                                                buttonAgain.setVisibility(VISIBLE);
                                            }
                                            else{
                                                t1.setText("Failed.Try again");
                                            }
                                        }
                                    });

                                }
                                else {
                                    t1.setText("Failed.Try again");
                                }
                            }
                        });



                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        buttonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price.setEnabled(true);
                buttonAdd.setVisibility(VISIBLE);
                spinner2.setEnabled(true);
                t1.setText("Edit Details");
                buttonAgain.setVisibility(GONE);

            }
        });

        return  view1;
    }
}
