package com.example.mychat.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.mychat.R;
import com.example.mychat.Utility;
import com.example.mychat.adapter.ChatAdapter;
import com.example.mychat.databinding.ActivityChatBinding;
import com.example.mychat.modal.Chats;
import com.example.mychat.modal.User;
import com.example.mychat.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private User recieveruser;

    private FirebaseDatabase database;

    private EditText EDTmessage;
    private User currentUser;

    private Chats currentchats=new Chats();
    private UserViewModel userViewModel;

    private ArrayList<Chats> chatsArrayList=new ArrayList<>();

    private FirebaseAuth auth;
    private String SenderUID;
    private String RecieverUID;
    private ChatActivityClickHandlers clickHandlers;
    private ActivityChatBinding activityChatBinding;

    private ChatAdapter chatAdapter;

    private  LinearLayoutManager linearLayoutManager;

private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        EDTmessage=findViewById(R.id.EDTmessage);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        //calling the adapter in getcurrent user function as the cureent user details is not
        //coming in time so shifting to the function
        getCurrentUser();
        recieveruser=getIntent().getParcelableExtra("user");
        RecieverUID=recieveruser.getUid();
        SenderUID=auth.getUid();

         linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        activityChatBinding= DataBindingUtil.setContentView(this,R.layout.activity_chat);
        activityChatBinding.setChats(currentchats);

        activityChatBinding.setUser(recieveruser);

        currentUser=User.getInstance();


        clickHandlers=new ChatActivityClickHandlers(this);

        activityChatBinding.setClickHandlers(clickHandlers);


        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);


       Log.v("dbharry",""+currentUser.getImageURI());


recyclerView=activityChatBinding.chatrecycler;

        userViewModel.getallChats(RecieverUID,SenderUID).observe(this, new Observer<ArrayList<Chats>>() {
            @Override
            public void onChanged(ArrayList<Chats> chats) {
                //reading all the chats and pasting it to the chats array list
                chatsArrayList.clear();
                for(Chats chats1 : chats) {
                    chatsArrayList.add(chats1);
                }

                setrecyclerview();



            }

        });





    }

    private void setrecyclerview() {

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    public  class ChatActivityClickHandlers{
        Context context;


        public ChatActivityClickHandlers(Context context) {
            this.context = context;
        }


        public void sendmessage(View view){
            String message=currentchats.getMessage();
            userViewModel.updatechats(message);
            currentchats.setMessage("");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void getCurrentUser(){



        DatabaseReference userreference= database.getReference().child(Utility.USER_COLUMN);

        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    User user=snapshot1.getValue(User.class);

                    if(user.getUid().equals(auth.getUid())){
                        currentUser=user;
                        chatAdapter=new ChatAdapter(getApplicationContext(),chatsArrayList,recieveruser,currentUser);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}