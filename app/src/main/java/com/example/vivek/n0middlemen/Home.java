package com.example.vivek.n0middlemen;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseUsers;
    TextView name;
    String mobile;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        Intent intent =  getIntent();
        //Toast.makeText(this,intent.getStringExtra("Mob"),Toast.LENGTH_SHORT).show();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setTitle("Chat");

        View header = navigationView.getHeaderView(0);
        name = header.findViewById(R.id.name);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void showData(DataSnapshot dataSnapshot){

        //Toast.makeText(this,firebaseUser.getPhoneNumber(),Toast.LENGTH_SHORT).show();

        if(dataSnapshot.child(firebaseUser.getPhoneNumber()).exists() == true){
            //user = dataSnapshot.child(firebaseUser.getPhoneNumber()).getValue(User.class);
            User user = new User();
            user = dataSnapshot.child(firebaseUser.getPhoneNumber()).getValue(User.class);
            name.setText(user.getName());

            if(user.getType().contentEquals("Customer")){
                navigationView.inflateMenu(R.menu.activity_home_drawer);
            }
            else{
                navigationView.inflateMenu(R.menu.activity_home_drawer1);
            }
        }
        //User user = dataSnapshot.child(firebaseUser.getPhoneNumber()).getValue(User.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(Home.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
            toolbar.setTitle("Dashboard");
            // Handle the camera action
        } else if (id == R.id.orders) {
            toolbar.setTitle("My Orders");

        } else if (id == R.id.buy) {
            toolbar.setTitle("Buy");
            buyFragment  buyFragment = new buyFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.home,buyFragment,buyFragment.getTag()).commit();

        } else if (id == R.id.edit) {
            toolbar.setTitle("Edit Profile");

        } else if (id == R.id.share) {
            toolbar.setTitle("Share");
        }else if(id == R.id.sell){
            toolbar.setTitle("Sell");
            SellFragment  sellFragment = new SellFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.home,sellFragment,sellFragment.getTag()).commit();
        }else if(id == R.id.crops){
            toolbar.setTitle("My Crops");
            CropsFragment mycrops = new CropsFragment();
            FragmentManager manager =getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.home,mycrops,mycrops.getTag()).commit();
        }
        else {
            toolbar.setTitle("FAQ");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
