package id.fathi.diffablecompanion.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import id.fathi.diffablecompanion.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView daftar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.button_login);
        daftar = (TextView) findViewById(R.id.daftar);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftar();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(email.getText().toString().trim(),password.getText().toString().trim());
            }
        });

    }

    public void daftar(){
        Intent regist = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(regist);
    }

    private void login (String username, String password){
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(home);
                }else {
                    Toast.makeText(LoginActivity.this, "Silahkan Cek Kembali Email dan Password anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
