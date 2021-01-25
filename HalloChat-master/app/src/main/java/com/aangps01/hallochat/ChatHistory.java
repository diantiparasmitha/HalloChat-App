package com.aangps01.hallochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aangps01.hallochat.Adapter.UserAdapter;
import com.aangps01.hallochat.Model.Chat;
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

public class ChatHistory extends AppCompatActivity {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    Button btn_home;

    private List<String> userList;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History Private Konseling");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatHistory.this));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    if(chat.getSender().equals(firebaseUser.getUid())){
                        userList.add(chat.getReceiver());
                    }

                    if(chat.getReceiver().equals(firebaseUser.getUid())){
                        userList.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatHistory.this, MainActivity.class));
            }
        });
    }

    private void readChats(){
        mUsers = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);

                    for(String id : userList){
                        if(user.getId().equals(id)){
                            if(mUsers.size() != 0){
                                for(User user1 : mUsers){
                                    if(!user.getId().equals(user1.getId())){
                                        mUsers.add(user);
                                    }
                                }
                            }else {
                                mUsers.add(user);
                            }
                        }
                    }
                }

                userAdapter = new UserAdapter(ChatHistory.this,mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}