package id.fathi.diffablecompanion.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.fathi.diffablecompanion.Model.Tamu;
import id.fathi.diffablecompanion.R;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, nama, ttl, nik, jeniskelamin, alamat, pekerjaan, nohp;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button daftar;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Tamu");
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        nama = (EditText) findViewById(R.id.nama);
        ttl = (EditText) findViewById(R.id.ttl);
        nik = (EditText) findViewById(R.id.nik);
        jeniskelamin = (EditText) findViewById(R.id.jeniskelamin);
        alamat = (EditText) findViewById(R.id.alamat);
        pekerjaan = (EditText) findViewById(R.id.pekerjaan);
        nohp = (EditText) findViewById(R.id.nohp);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        daftar = (Button) findViewById(R.id.daftar);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetworkConnection()){
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);
                    String jenis = radioButton.getText().toString();
                    Register(email.getText().toString(), password.getText().toString(), nama.getText().toString(), ttl.getText().toString(),
                            nik.getText().toString(), jeniskelamin.getText().toString(), alamat.getText().toString(), pekerjaan.getText().toString(),
                            nohp.getText().toString(), jenis);

                }else{
                    Toast.makeText(RegisterActivity.this, "Tidak Dapat Terhubung Ke Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Register(String email, String password, String nama, String ttl, String nik, String jeniskelamin, String alamat, String pekerjaan,
                          String nohp, String jenis){
        String registerId = mDatabaseRef.push().getKey();
        Tamu tamu = new Tamu(registerId, email, password, nama, ttl, nik, jeniskelamin, alamat, pekerjaan, nohp, jenis);
        mDatabaseRef.child(registerId).setValue(tamu).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil, Silahkan Cek Email Anda", Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Tidak Dapat Terhubung Ke Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
