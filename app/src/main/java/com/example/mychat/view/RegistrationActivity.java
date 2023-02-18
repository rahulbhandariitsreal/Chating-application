package com.example.mychat.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mychat.R;
import com.example.mychat.databinding.ActivityRegistrationBinding;
import com.example.mychat.modal.User;
import com.example.mychat.viewmodel.UserViewModel;

public class RegistrationActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private User newuser;

    private Uri selectedImageuri;

    public static final String USER_KEY="myuser";

private ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
     userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
     newuser=User.getInstance();
     newuser.setStatus("hey i am useing this application");

     binding.profileImageReciever.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent i=new Intent();
             i.setType("image/*");
             i.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(Intent.createChooser(i,"Select Picture"),10);
         }
     });

     binding.signup.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String email=binding.ETemailReg.getText().toString();
             String password=binding.ETpassReg.getText().toString();
             String username=binding.ETUsernameReg.getText().toString();

             if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)||TextUtils.isEmpty(username)){
                 Toast.makeText(RegistrationActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
             } else if (password.length()<6) {
                 Toast.makeText(RegistrationActivity.this, "MIN charter 6", Toast.LENGTH_SHORT).show();
                 binding.ETpassReg.setError("Invalid password");
             }else {
                 newuser.setName(username);
                 newuser.setEmail(email);
                 newuser = userViewModel.Registration(newuser, password, selectedImageuri);
                 if (newuser.getName() != "failed") {
                     new Handler().postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             // on below line we are
                             // creating a new intent
                             Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                             startActivity(i);
                             // on below line we are
                             // starting a new activity.
                             startActivity(i);
                             // on the below line we are finishing
                             // our current activity.
                             finish();
                         }
                     }, 5000);
                 } else {
                     Toast.makeText(RegistrationActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                     return;
                 }
             }
         }
     });


     binding.btnSignin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
         }
     });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data !=null && resultCode==RESULT_OK){
                selectedImageuri=data.getData();
                binding.profileImageReciever.setImageURI(selectedImageuri);
            }
        }
    }
}