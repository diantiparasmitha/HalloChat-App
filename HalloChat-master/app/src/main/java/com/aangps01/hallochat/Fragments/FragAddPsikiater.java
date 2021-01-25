package com.aangps01.hallochat.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aangps01.hallochat.LoginActivity;
import com.aangps01.hallochat.R;
import com.aangps01.hallochat.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.aangps01.hallochat.RegisterActivity;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragAddPsikiater#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragAddPsikiater extends Fragment {

    MaterialEditText email,username,password;
    Button btn_register;

    FirebaseAuth mAuth;
    DatabaseReference reference;

    public FragAddPsikiater() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragAddPsikiater newInstance() {
        FragAddPsikiater fragment = new FragAddPsikiater();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_psikiater, container, false);
        email = view.findViewById(R.id.email);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        btn_register = view.findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getContext(), "All fileds are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6 ){
                    Toast.makeText(getContext(), "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password);
                }
            }
        });

        return view;

    }

    private void register(final String username, final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("email",email);
                            hashMap.put("username",username);
                            hashMap.put("role","psikiater");
                            hashMap.put("imageURL","default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
//                                        Intent intent = new Intent(getContext(),LoginActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
//                                        FirebaseAuth.getInstance().signOut();
//                                        startActivity(intent);
                                        Toast.makeText(getContext(),"Register Success. You Can Login Now",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(),"You can't register with this email or username",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}