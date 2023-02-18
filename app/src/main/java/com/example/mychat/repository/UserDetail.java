package com.example.mychat.repository;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mychat.Utility;
import com.example.mychat.modal.User;
import com.example.mychat.view.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserDetail {

    //for storing user information and chat information

    private FirebaseDatabase database;

    private int resultofupload;
    //for authentication
    private FirebaseAuth auth;

    //for storing images
    private FirebaseStorage storage;

    private User currentUser, nowuser;


    private StorageReference storageReference;
    private DatabaseReference databasereference;
    private Application application;
    private int res;


    public UserDetail(Application application) {
        this.application = application;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        currentUser = User.getInstance();
    }

    //used for registration
    public User RegisterUser(User newuser, final String password, Uri selectedimageURI) {

        auth.createUserWithEmailAndPassword(newuser.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.v("dbharry", "authentication comple");
                    //making the reference for both the database
                    databasereference = database.getReference().child(Utility.USER_COLUMN).child(auth.getUid());
                    storageReference = storage.getReference().child(Utility.UPLOAD_COLUMN_PIC).child(auth.getUid());

                    newuser.setUid(auth.getUid());

                    if (selectedimageURI != null) {
                        //uploading the image in the storage
                        storageReference.putFile(selectedimageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {


                                        String imageuri = uri.toString();

                                        newuser.setImageURI(imageuri);
                                        currentUser = newuser;
                                        //now uploading the user details in the database
                                        databasereference.setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.v("dbharry", "user uploaded complete");
                                                    //   Toast.makeText(context, "Successfully Registered", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Log.v("dbharry", "user not uploaded complete");
                                                    //    Toast.makeText(context, "Failed to Stored", Toast.LENGTH_SHORT).show();
                                                    currentUser.setName("failed");
                                                }
                                            }
                                        });

                                    }
                                });
                            }
                        });
                    } else {
                        //uploading the default image in the storage
                        String imageUri = "https://firebasestorage.googleapis.com/v0/b/mychat-35b9c.appspot.com/o/defaultprofile.png?alt=media&token=7fbf6884-ffb6-4550-ace7-a0823b063a4f";
                        newuser.setImageURI(imageUri);
                        currentUser = newuser;
                        databasereference.setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //      Toast.makeText(context, "Succesfully Registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    //  Toast.makeText(context, "Failed to Stored", Toast.LENGTH_SHORT).show();
                                    currentUser.setName("failed");
                                }
                            }
                        });
                    }


                } else {
                    //task is unsuccesfull
                    Log.v("dbharry", "Failed to register");
                    currentUser.setName("failed");
                }
            }
        });

        return currentUser;

    }

    public int updateUser(Uri selectedimageuri, User user) {

        //reference is user for writng and upload ing the things in the databse
        databasereference = database.getReference().child(Utility.USER_COLUMN).child(user.getUid());
        storageReference = storage.getReference().child(Utility.UPLOAD_COLUMN_PIC).child(user.getUid());


        if (selectedimageuri != null) {
            storageReference.putFile(selectedimageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String urinow = uri.toString();
                                user.setImageURI(urinow);
                                databasereference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.v("dbharry", "Uploaded Succesfully");
                                            resultofupload = 1;
                                        } else {
                                            resultofupload = 0;
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        } else {

            databasereference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.v("dbharry", "Uploaded Succesfully");
                        resultofupload = 1;
                    } else {
                        resultofupload = 0;
                    }
                }
            });

        }
        //    database.getReference().child(Utility.USER_COLUMN).child(user.getUid()).
        return resultofupload;
    }

    public int LoginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(context, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    res = 1;
                } else {
                    //    Toast.makeText(context, "Failed to login", Toast.LENGTH_SHORT).show();
                    res = -1;

                }
            }
        });
        return res;
    }

    public User getCurrentUser() {

        nowuser = User.getInstance();

        DatabaseReference userreference = database.getReference().child(Utility.USER_COLUMN);

        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);

                    if (user.getUid().equals(auth.getUid())) {
                        nowuser = user;
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return nowuser;

    }


}
