package id.fathi.diffablecompanion.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.fathi.diffablecompanion.R;
import id.fathi.diffablecompanion.Controller.Adapter.ViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    Switch switchStatus;
    TextView textStatus;
    Button logout;
    FirebaseAuth firebaseAuth;
    String uid, status;
    DatabaseReference databaseReferenceDifabel, databaseReferencePendamping, databaseReferenceCompanion;
    Location location;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");
        databaseReferenceCompanion = FirebaseDatabase.getInstance().getReference("Companion");
        firebaseAuth = FirebaseAuth.getInstance();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabBar);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);

        logout = (Button) findViewById(R.id.logout);
        switchStatus = (Switch) findViewById(R.id.switchStatus);
        textStatus = (TextView) findViewById(R.id.textStatus);

        viewPager2.setAdapter(new ViewPagerAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0 : {
                        tab.setText("Cari");
                        break;
                    }
                    case 1 : {
                        final BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setVisible(false);
                        tab.setText("Companion");
                        databaseReferenceCompanion.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())){
                                    badgeDrawable.setVisible(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();

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
                            if (status.equals("Tersedia")){
                                switchStatus.setChecked(true);
                            }
                            if (status.equals("Tidak Tersedia")){
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
