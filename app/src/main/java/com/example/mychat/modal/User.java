package com.example.mychat.modal;


import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import androidx.databinding.library.baseAdapters.BR;

import de.hdodenhof.circleimageview.CircleImageView;

public class User extends BaseObservable implements Parcelable {



    private String uid;
    private String name;
    private String email;
    private String imageURI;

    private static User Instance;



    public static User getInstance(){
        if (Instance == null){
            Instance=new User();
        }
        return Instance;
    }

    @BindingAdapter("imageURI")
    public static void loadImage(ImageView imageView, String imageURI){
        Glide.with(imageView.getContext()).load(imageURI).into(imageView);

    }

    private String status;

    public User() {
    }

    public User(String uid, String name, String email, String imageURI,String status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageURI = imageURI;
        this.status=status;
    }

    protected User(Parcel in) {
        uid = in.readString();
        name = in.readString();
        email = in.readString();
        imageURI = in.readString();
        status = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Bindable
    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
notifyPropertyChanged(BR.uid);
    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }


    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }


    @Bindable
    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
        notifyPropertyChanged(BR.imageURI);
    }


    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(imageURI);
        dest.writeString(status);
    }
}

