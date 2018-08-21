package com.example.vivek.n0middlemen;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class buyFragment extends Fragment {

    Spinner spinner1,spinner2;

    ListView listView;
    Button buttonSearch,buttonAgain;
    String cropname,districtname;
    FirebaseAuth mAuth;
    DatabaseReference dBUsers,dBCrops;
    FirebaseUser fbUser;
    CardView card;
    EditText min,max;
    long minm,maxm;
    int count;

    private List<Crop> cropdetails;

    public buyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 = inflater.inflate(R.layout.fragment_buy, container, false);

        mAuth = FirebaseAuth.getInstance();

        dBCrops = FirebaseDatabase.getInstance().getReference("Rice");

        cropdetails =  new ArrayList<>();
        fbUser = mAuth.getCurrentUser();
        listView = (ListView) view1.findViewById(R.id.listview);
        buttonSearch = (Button) view1.findViewById(R.id.buttonSearch);
        buttonAgain =(Button) view1.findViewById(R.id.buttonAgain);
        spinner1 = (Spinner) view1.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view1.findViewById(R.id.spinner2);
        card = (CardView) view1.findViewById(R.id.card);
        min = (EditText) view1.findViewById(R.id.min);
        max = (EditText) view1.findViewById(R.id.max);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.district, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.crops, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner2.setAdapter(adapter1);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ArrayList<Crop> arrayList = new ArrayList<>();
                cropname = spinner2.getSelectedItem().toString();
                districtname = spinner1.getSelectedItem().toString();
                //Toast.makeText(buyFragment.this.getActivity(),cropname +" "+ districtname,Toast.LENGTH_SHORT).show();
                dBUsers = FirebaseDatabase.getInstance().getReference(cropname);
                listView.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(min.getText().toString())){
                    minm = Integer.parseInt(min.getText().toString());
                }else{
                    minm = 0;
                }

                if(!TextUtils.isEmpty(max.getText().toString())){
                    maxm = Integer.parseInt(max.getText().toString());
                }else{
                    maxm = 1000000000;
                }
                card.setVisibility(GONE);
                buttonAgain.setVisibility(View.VISIBLE);
                count =0;
                dBUsers.orderByChild("Price").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        cropdetails.clear();


                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            Crop crop = new Crop();
                            crop = ds.getValue(Crop.class);

                            int price = Integer.parseInt(crop.getPrice());
                            if(crop.getDistrict().contentEquals(districtname) && price >=minm && price <=maxm){
                                cropdetails.add(crop);
                                count+=1;
                            }
                        }
                        Toast.makeText(buyFragment.this.getActivity(),Integer.toString(count),Toast.LENGTH_SHORT).show();
                        CropList arrayAdapter = new CropList(buyFragment.this.getActivity(), cropdetails);
                        listView.setAdapter(arrayAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                dBUsers.orderByChild("price").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot ds : dataSnapshot.getChildren()){
//                            Crop crop = ds.getValue(Crop.class);
//                            Toast.makeText(buyFragment.this.getActivity(),crop.getName(),Toast.LENGTH_SHORT).show();
//                            if(crop.getDistrict().contentEquals(districtname)){
//                            }
//                        }
//                        ArrayAdapter<Crop> arrayAdapter = new ArrayAdapter<Crop>(buyFragment.this.getActivity(),android.R.layout.simple_list_item_2,arrayList);
//                        listView.setAdapter(arrayAdapter);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
            }
        });


        buttonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(GONE);
                card.setVisibility(View.VISIBLE);
                buttonAgain.setVisibility(GONE);

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Crop crop = cropdetails.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(buyFragment.this.getActivity());

                View view2 = LayoutInflater.from(buyFragment.this.getActivity()).inflate(R.layout.custom_alert,null);

                TextView title = (TextView) view2.findViewById(R.id.textViewTitleAlert);
                title.setText("Contact " + crop.getName());

                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phone = crop.getMobile();
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                                "tel", phone, null));
                        startActivity(phoneIntent);
                    }
                });

                builder.setNegativeButton("Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phone = crop.getMobile();
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("sms:" + phone ));
                        sendIntent.putExtra("sms_body","Hi " + crop.getName()+" ,");
                        startActivity(sendIntent);
                    }
                });

                builder.setView(view2);
                builder.show();
            }
        });
        return  view1;
    }
}
