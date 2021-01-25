package com.aangps01.hallochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aangps01.hallochat.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email, password;
    Button btn_login;
    TextView toRegister;
    String role;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ConnectivityManager connectivityManager;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (isNetworkAvailable()) {
            if (firebaseUser != null) {
                reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        role = user.getRole();

                        if (role.equals("user")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else if (role.equals("admin")) {
                            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                finish();
            }
        } else {
            Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        toRegister = findViewById(R.id.toRegister);

        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (isNetworkAvailable()) {
                    if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                        Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                                            reference.addValueEventListener(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    User user = snapshot.getValue(User.class);
                                                    role = user.getRole();
                                                    if (role.equals("user")) {
                                                        Intent intent = new Intent(LoginActivity.this, WelcomeOne.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    } else if (role.equals("admin")) {
                                                        Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }else if (role.equals("psikiater")){
                                                        Intent intent = new Intent(LoginActivity.this, PsikiaterMainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });

                                        } else {
                                            Toast.makeText(LoginActivity.this, "Authentication Failed. Wrong Username or Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}