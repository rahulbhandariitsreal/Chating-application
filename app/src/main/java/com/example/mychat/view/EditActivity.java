package com.example.mychat.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mychat.R;
import com.example.mychat.Utility;
import com.example.mychat.adapter.ChatAdapter;
import com.example.mychat.databinding.ActivityEditBinding;
import com.example.mychat.modal.User;
import com.example.mychat.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {


    User currentUser;

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private CircleImageView circleImageView;

    private Uri selectedImageuri;

    private EditActivityClickHandlers clickme;
    private ActivityEditBinding activityEditBinding;
    private UserViewModel userViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        circleImageView=findViewById(R.id.image_user);


        activityEditBinding= DataBindingUtil.setContentView(this,R.layout.activity_edit);
        clickme=new EditActivityClickHandlers(this);
        activityEditBinding.setClickme(clickme);
        getCurrentUser();


        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);



    }

    public class EditActivityClickHandlers{
        Context context;

        public EditActivityClickHandlers(Context context) {
            this.context = context;
        }
        public void saveData(View view){
            Log.v("dbharry",currentUser.getName()+"\n"+currentUser.getStatus());
int Resultnow=userViewModel.updateUser(selectedImageuri,currentUser);

if(Resultnow==1){
    Log.v("dbharry","Changes made");
    Toast.makeText(context, "Saved changes", Toast.LENGTH_SHORT).show();

}

        }
        public void saveimage(View view){
            Intent i=new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i,"Select Picture"),13);
        }
        public void backgo(View view){
            onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==13){
            if(data !=null && resultCode==RESULT_OK){
                selectedImageuri=data.getData();
              //  binding.profileImageReciever.setImageURI(selectedImageuri);
                int fruit=userViewModel.updateUser(selectedImageuri,currentUser);
                if (fruit==1){
                    Log.v("dbharry","Succelfullt uploaded");
                }
            }
        }
    }
    public void getCurrentUser(){

database=FirebaseDatabase.getInstance();
auth=FirebaseAuth.getInstance();

        DatabaseReference userreference= database.getReference().child(Utility.USER_COLUMN);

        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    User user=snapshot1.getValue(User.class);

                    if(user.getUid().equals(auth.getUid())){
                        currentUser=user;
                        activityEditBinding.setUser(currentUser);
                        circleImageView.setImageURI(selectedImageuri);
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