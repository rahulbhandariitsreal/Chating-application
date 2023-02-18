package com.example.mychat.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mychat.Utility;
import com.example.mychat.modal.Chats;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class ChatRepo {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;

    private DatabaseReference chatreference;

    public static String senderRoom;

    public static String recieverRoom;

    private String senderUID;
    private String recieverUID;

    private ArrayList<Chats> chats = new ArrayList<>();


    private MutableLiveData<ArrayList<Chats>> mutableLiveDatachats = new MutableLiveData<>();
    private Application application;
    String imageuri;

    public ChatRepo(Application application) {
        this.application = application;
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        //initialization of the room

    }

    public LiveData<ArrayList<Chats>> getallChats( String recieverUID, String senderUID) {
        this.senderUID=senderUID;
        this.recieverUID=recieverUID;
        senderRoom = senderUID + recieverUID;
        recieverRoom = recieverUID + senderUID;

        chatreference = database.getReference().child(Utility.CHAT_COLUMN).child(senderRoom).child(Utility.MESSAGE_COLUMN);
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
chats.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chats chats1 = snapshot1.getValue(Chats.class);
                    chats.add(chats1);
                }
                mutableLiveDatachats.setValue(chats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mutableLiveDatachats;

    }

    public void updatechats(String message) {

        Date now=new Date();
        Chats curentchat=new Chats(message, senderUID, now.getTime());

        //uploading cahts in the sender room
        database.getReference().child(Utility.CHAT_COLUMN).child(senderRoom).child(Utility.MESSAGE_COLUMN).push().
                setValue(curentchat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //uploading chat in recierver room
database.getReference().child(Utility.CHAT_COLUMN).child(recieverRoom).child(Utility.MESSAGE_COLUMN).push()
        .setValue(curentchat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

            }
        });

    }

    public  String getSenderimageuri(){
        databaseReference=database.getReference().child(Utility.UPLOAD_COLUMN_PIC).child(senderUID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 imageuri=snapshot.child("imageURI").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
return  imageuri;
    }
}
