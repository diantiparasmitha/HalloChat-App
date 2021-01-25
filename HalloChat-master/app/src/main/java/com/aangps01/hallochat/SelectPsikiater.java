package com.aangps01.hallochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aangps01.hallochat.Adapter.PsikiaterAdapter;
import com.aangps01.hallochat.Adapter.SelectPsikiaterAdapter;
import com.aangps01.hallochat.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class SelectPsikiater extends AppCompatActivity {

    private RecyclerView recyclerView;

    private SelectPsikiaterAdapter selectPsikiaterAdapter;
    private List<User> mUsers;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_psikiater);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pilih Psikiater");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectPsikiater.this,MainActivity.class));
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectPsikiater.this));
        mUsers = new ArrayList<>();
        readUser();
        selectPsikiaterAdapter = new SelectPsikiaterAdapter(SelectPsikiater.this,mUsers);
        recyclerView.setAdapter(selectPsikiaterAdapter);


    }

    private void readUser(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    Log.d("User","Email"+user.getEmail());
                    if(user.getRole().equals("psikiater")){
                        mUsers.add(user);
                    }
//                    if(!user.getId().equals(firebaseUser.getUid())){
//                        mUsers.add(user);
//                    }
                    selectPsikiaterAdapter = new SelectPsikiaterAdapter(SelectPsikiater.this,mUsers);
                    recyclerView.setAdapter(selectPsikiaterAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}