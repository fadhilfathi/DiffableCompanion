package id.fathi.diffablecompanion.View.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.fathi.diffablecompanion.Model.User;
import id.fathi.diffablecompanion.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    Button chat;
    TextView nama, nohp;
    DatabaseReference databaseReferenceDifabel, databaseReferencePendamping;
    User user;
    String uidreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");

        chat = (Button) findViewById(R.id.chat);
        nama = (TextView) findViewById(R.id.nama);
        nohp = (TextView) findViewById(R.id.nohp);

        uidreceiver = getIntent().getStringExtra("uid");

        databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uidreceiver)){
                    databaseReferenceDifabel.child(uidreceiver).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);

                            nama.setText(user.getNama());
                            nohp.setText(user.getNohp());
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
                if(dataSnapshot.hasChild(uidreceiver)){
                    databaseReferencePendamping.child(uidreceiver).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);

                            nama.setText(user.getNama());
                            nohp.setText(user.getNohp());
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

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(MapsActivity.this,ChatActivity.class);
                chat.putExtra("uid",uidreceiver);
                startActivity(chat);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");

        databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uidreceiver)){
                    databaseReferenceDifabel.child(uidreceiver).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);

                            LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
                            map.addMarker(new MarkerOptions().position(latLng).title("Lokasi Saat Ini"));
                            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
                if(dataSnapshot.hasChild(uidreceiver)){
                    databaseReferencePendamping.child(uidreceiver).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);

                            LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
                            map.addMarker(new MarkerOptions().position(latLng).title("Lokasi Saat Ini"));
                            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

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
    }
}
