package com.example.mychat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.databinding.ItemUserRowBinding;
import com.example.mychat.modal.User;
import com.example.mychat.view.ChatActivity;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private ArrayList<User> userArrayList;
private Context context;


    public UserAdapter(ArrayList<User> userArrayList,Context context) {
        userArrayList.clear();
        this.userArrayList = userArrayList;
      this.context=context;
    }

    public void updatechatsuser(ArrayList<User> userArrayList){
       this.userArrayList.clear();
       this.userArrayList=userArrayList;
       notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         ItemUserRowBinding itemUserRowBinding= DataBindingUtil.inflate
                 (LayoutInflater.from(parent.getContext()),
                 R.layout.item_user_row,parent
                 ,false);
        return new ViewHolder(itemUserRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=userArrayList.get(position);
        holder.itemUserRowBinding.setUser(user);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ItemUserRowBinding itemUserRowBinding;

        public ViewHolder(ItemUserRowBinding itemUserRowBinding) {
            super(itemUserRowBinding.getRoot());
            this.itemUserRowBinding = itemUserRowBinding;

            itemUserRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

int position=getAdapterPosition();
User curuser=userArrayList.get(position);
if(position !=RecyclerView.NO_POSITION){
    Intent intent=new Intent(context, ChatActivity.class);
    intent.putExtra("user",curuser);
    context.startActivity(intent);
    Toast.makeText(context, "Going to chat", Toast.LENGTH_SHORT).show();
}
                }
            });

        }
    }
}
