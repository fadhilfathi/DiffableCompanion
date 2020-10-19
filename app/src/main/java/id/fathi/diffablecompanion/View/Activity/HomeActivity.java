package id.fathi.diffablecompanion.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.fathi.diffablecompanion.R;
import id.fathi.diffablecompanion.View.Adapter.ViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    Switch switchStatus;
    TextView textStatus;
    Button logout;
    FirebaseAuth firebaseAuth;
    String uid, status;
    DatabaseReference databaseReferenceDifabel, databaseReferencePendamping;
    Location location;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");
        firebaseAuth = FirebaseAuth.getInstance();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabBar);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        logout = (Button) findViewById(R.id.logout);
        switchStatus = (Switch) findViewById(R.id.switchStatus);
        textStatus = (TextView) findViewById(R.id.textStatus);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        uid = FirebaseAuth.getInstance().getUid();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }else{
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    final Location current = task.getResult();
                    databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(uid)){
                                databaseReferenceDifabel.child(uid).child("latitude").setValue(current.getLatitude());
                                databaseReferenceDifabel.child(uid).child("longitude").setValue(current.getLongitude());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    databaseReferencePendamping.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(uid)){
                                databaseReferencePendamping.child(uid).child("latitude").setValue(current.getLatitude());
                                databaseReferencePendamping.child(uid).child("longitude").setValue(current.getLongitude());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){
                    databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            status = dataSnapshot.child(uid).child("status").getValue().toString().trim();
                            textStatus.setText(status);
                            if (status.equals("Perlu Pendamping")){
                                switchStatus.setChecked(true);
                            }
                            if (status.equals("Tidak Perlu Pendamping")){
                                switchStatus.setChecked(false);
                            }
                            switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b){
                                        databaseReferenceDifabel.child(uid).child("status").setValue("Perlu Pendamping");
                                        textStatus.setText("Perlu Pendamping");
                                    }
                                    if (!b){
                                        databaseReferenceDifabel.child(uid).child("status").setValue("Tidak Perlu Pendamping");
                                        textStatus.setText("Tidak Perlu Pendamping");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReferencePendamping.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){
                    databaseReferencePendamping.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            status = dataSnapshot.child(uid).child("status").getValue().toString().trim();
                            textStatus.setText(status);
                            if (status.equals("Perlu Pendamping")){
                                switchStatus.setChecked(true);
                            }
                            if (status.equals("Tidak Perlu Pendamping")){
                                switchStatus.setChecked(false);
                            }
                            switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b){
                                        databaseReferencePendamping.child(uid).child("status").setValue("Tersedia");
                                        textStatus.setText("Tersedia");
                                    }
                                    if (!b){
                                        databaseReferencePendamping.child(uid).child("status").setValue("Tidak Tersedia");
                                        textStatus.setText("Tidak Tersedia");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

    }
}
