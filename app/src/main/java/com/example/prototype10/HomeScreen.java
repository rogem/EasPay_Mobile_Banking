package com.example.prototype10;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    FirebaseAuth mAuth;
    private NavigationView nav_view;
    private long pressedTime;

    private TextView nav_email, nav_fname, nav_lname,nav_balance;
    private DatabaseReference userRef, userReff;

    String email,fname,lname,balance,setbalance,mobileNo;
    String useremail,key,FirstName;
    Bundle userbundle,bundle;

    Double getbalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_view = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bundle = getIntent().getExtras();
        useremail = bundle.getString("Email");

        nav_balance = findViewById(R.id.nav_Balance);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_fname = nav_view.getHeaderView(0).findViewById(R.id.nav_user_fname);
        nav_lname = nav_view.getHeaderView(0).findViewById(R.id.nav_user_lname);


        userReff = FirebaseDatabase.getInstance().getReference();
        Query query = userReff.child("User").orderByChild("Email").equalTo(useremail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    key = ds.getKey();
                    userRef = FirebaseDatabase.getInstance().getReference().child("User").child(
                            key
                    );
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()){

                                email = snapshot.child("Email").getValue().toString();
                                fname = snapshot.child("FirstName").getValue().toString();
                                lname = snapshot.child("LastName").getValue().toString();
                                balance = snapshot.child("Balance").getValue().toString();
                                mobileNo = snapshot.child("ContactNumber").getValue().toString();
                                

                                getbalance = Double.parseDouble(balance);
                                setbalance = String.valueOf(getbalance);

                                FirstName = fname;
                                nav_email.setText(mobileNo);
                                nav_fname.setText(fname);
                                nav_lname.setText(lname);
                                nav_balance.setText(setbalance);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userbundle = new Bundle();
        userbundle.putString("FirstName", FirstName);
        userbundle.putString("Email", useremail);

    }




    public void btnsend(View view) {
        Toast.makeText(this, "Send Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SendMoney.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }

    public void btnpay(View view) {
        Toast.makeText(this, "Pay Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Pay.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }
    public void btnCashIn(View view) {
        Toast.makeText(this, "CashIn Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),CashIn.class);
        intent.putExtras(userbundle);
        startActivity(intent);
//        Intent intent = new Intent(this, CashIn.class);
        startActivity(intent);
    }
    public void btnCashOut(View view) {
        Toast.makeText(this, "CashOut Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),CashOut.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }
    public void btntrnsctn(View view) {
        Toast.makeText(this, "Transaction Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),TransactionHistory.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }


    public void btnpoints(View view) {
        Toast.makeText(this, "Points Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),Points.class);
        intent.putExtras(userbundle);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_editprofile:
                Intent intentep = new Intent(getApplicationContext(),EditInfo.class);
                intentep.putExtras(userbundle);
                startActivity(intentep);
                break;
            case R.id.nav_print:
                Intent intenteps = new Intent(getApplicationContext(),Print.class);
                intenteps.putExtras(userbundle);
                startActivity(intenteps);
                break;
            case R.id.nav_settings:
                Intent intents = new Intent(getApplicationContext(),Setting.class);
                intents.putExtras(userbundle);
                startActivity(intents);
                break;
            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent=new Intent(HomeScreen.this,LogIn.class);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void nav_signout () {
        FirebaseAuth mAuth = FirebaseAuth.getInstance ();
        finish ();
        startActivity ( new Intent ( this, LogIn.class ) );
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}