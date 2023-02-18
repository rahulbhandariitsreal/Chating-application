package com.example.mychat.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychat.R;
import com.example.mychat.adapter.UserAdapter;
import com.example.mychat.databinding.ActivityHomeBinding;
import com.example.mychat.modal.User;
import com.example.mychat.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    private User currentuser;

    private FirebaseAuth auth;
    private ActivityHomeBinding binding;
    private UserViewModel userViewModel;
    private ArrayList<User> userArrayList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //current user is recieved by parcel
        currentuser=User.getInstance();
        currentuser=getIntent().getParcelableExtra(RegistrationActivity.USER_KEY);

        auth=FirebaseAuth.getInstance();

        userArrayList=new ArrayList<>();

        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);

        userAdapter=new UserAdapter(userArrayList,this);

     fetchdata();




        binding.imglogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                // Get the layout inflater
                LayoutInflater inflater = (HomeActivity.this).getLayoutInflater();
               View view= inflater.inflate(R.layout.logout_builder, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the
                // dialog layout
                TextView logout= view.findViewById(R.id.yes_logout);
                TextView nologout=view.findViewById(R.id.no_logout);

                builder.setTitle("LogOut");
                builder.setIcon(R.drawable.logout);
                builder.setView(view);
                AlertDialog dialog=builder.create();
                dialog.show();
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(auth.getCurrentUser() != null){
                            auth.signOut();
                            Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(HomeActivity.this,LoginActivity.class));

                        }
                    }
                });

                nologout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(HomeActivity.this, "No changes", Toast.LENGTH_SHORT).show();
                     dialog.dismiss();
                   //    dialog.cancel();
                    }
                });

            }
        });
binding.settingbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            Intent intent=new Intent(HomeActivity.this,EditActivity.class);
        intent.putExtra("user",currentuser);
        startActivity(intent);
    }
});

    }

    private void fetchdata(){


        userViewModel.getalluserDetails().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {

              userArrayList.clear();

                for(User user:users) {

                    userArrayList.add(user);

                }

                showOnrecycler();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showOnrecycler() {
        binding.mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainUserRecyclerView.setHasFixedSize(true);
        binding.mainUserRecyclerView.setAdapter(userAdapter);

        userAdapter.notifyDataSetChanged();
    }
}