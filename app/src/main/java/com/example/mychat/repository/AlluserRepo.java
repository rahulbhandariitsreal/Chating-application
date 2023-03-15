package com.example.mychat.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mychat.Utility;
import com.example.mychat.modal.User;
import com.example.mychat.view.RegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class AlluserRepo {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;

    private StorageReference storageReference;
    private MutableLiveData<ArrayList<User>> arrayListLiveDatauserlist=new MutableLiveData<>();
    private ArrayList<User> userArrayList;

private Application application;




    public AlluserRepo(Application application){
        this.application=application;
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        userArrayList=new ArrayList<>();
    }

    public  LiveData<ArrayList<User>> getAlluserDetails(){

        //getting all the user information


        databaseReference=database.getReference().child(Utility.USER_COLUMN);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    //adding all the users
                    User user=snapshot1.getValue(User.class);
                    assert user != null;
                    if(!user.getUid().equals(Objects.requireNonNull(auth.getCurrentUser()).getUid()))
                        userArrayList.add(user);
                }
                arrayListLiveDatauserlist.setValue(userArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

return  arrayListLiveDatauserlist;
    }

}
