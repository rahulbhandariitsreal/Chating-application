package com.example.mychat.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mychat.modal.Chats;
import com.example.mychat.modal.User;
import com.example.mychat.repository.AlluserRepo;
import com.example.mychat.repository.ChatRepo;
import com.example.mychat.repository.UserDetail;

import java.util.ArrayList;

public class UserViewModel extends AndroidViewModel {


    private User currentUser;
    private final UserDetail userDetail;
    private final  AlluserRepo alluserRepo;

    private  final ChatRepo chatRepo;


    public UserViewModel(@NonNull Application application) {
        super(application);
        userDetail = new UserDetail(application);
        alluserRepo = new AlluserRepo(application);
        chatRepo = new ChatRepo(application);
        currentUser = User.getInstance();
    }

    public LiveData<ArrayList<Chats>> getallChats(String recieverUID, String senderUID) {
        return chatRepo.getallChats(recieverUID, senderUID);
    }

    public void updatechats(String message) {
        chatRepo.updatechats(message);
    }

    public String getSencerImageURI() {
        return chatRepo.getSenderimageuri();
    }


    public LiveData<ArrayList<User>> getalluserDetails() {
        return alluserRepo.getAlluserDetails();
    }

    public int Login(String email, String passwrod) {
        int res;
        res = userDetail.LoginUser(email, passwrod);
        return res;
    }

    public User Registration(User newuser, String password, Uri selectedimageURI) {

        currentUser = userDetail.RegisterUser(newuser, password, selectedimageURI);
        return currentUser;
    }

    public int updateUser(Uri selectedImageuri, User user) {

        return userDetail.updateUser(selectedImageuri, user);
    }

    public User getnowUser() {

        User myuser = new User();
        myuser = userDetail.getCurrentUser();
        Log.v("dbharry", myuser.getName());
        Log.v("dbharry", myuser.getEmail());
        Log.v("dbharry", myuser.getStatus());
        Log.v("dbharry", myuser.getUid());
        Log.v("dbharry", myuser.getImageURI());
        return myuser;
    }

}
