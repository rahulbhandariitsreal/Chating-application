package com.example.mychat.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mychat.R;
import com.example.mychat.Utility;
import com.example.mychat.databinding.ActivityLoginBinding;
import com.example.mychat.modal.User;
import com.example.mychat.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private User user=new User();
    private ActivityLoginBinding binding;

    private FirebaseAuth.AuthStateListener authStateListener;

private  int res;
    private FirebaseUser Currentuser;
    private FirebaseAuth auth;
    private User newuser;




    @Override
    protected void onStart() {
        super.onStart();

       Currentuser=auth.getCurrentUser();
     auth.addAuthStateListener(authStateListener);
        if(Currentuser!=null){
startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Currentuser=firebaseAuth.getCurrentUser();
                if (Currentuser!=null){
                    //user already logged in
                }else{
                    //no use yet logged in
                }
            }
        };

        newuser=User.getInstance();
        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);

        binding.btnSignupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });


        binding.signinbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.ETemailLog.getText().toString();
                String passwrod=binding.ETpassLogin.getText().toString();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(passwrod)){
                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }else {
                     res = userViewModel.Login(email, passwrod);

                    if(res==-1){
                        Toast.makeText(LoginActivity.this, "Failed to login in ", Toast.LENGTH_SHORT).show();
                        binding.ETemailLog.setError("Inavlid Email");
                        binding.ETpassLogin.setError("Inavlid Password");
                    }else if(res==1) {
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        i.putExtra(Utility.USER_KEY, newuser);
                        startActivity(i);
                    }
                }
            }
        });

    }
}