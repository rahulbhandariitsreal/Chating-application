package com.example.mychat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;
import com.example.mychat.databinding.RecieverLayoutItemBinding;
import com.example.mychat.databinding.SenderLayoutItemBinding;
import com.example.mychat.modal.Chats;
import com.example.mychat.modal.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Chats> chatsArrayList;


  private User SenderUser;
  private User Recieveruser;

  private final int SENDER_ITEM=1;
  private final int RECIEVER_ITEM=2;

    public ChatAdapter(Context context, ArrayList<Chats> chatsArrayList,User Recieveruser,User SenderUser) {

        this.context = context;
        this.chatsArrayList = chatsArrayList;
        this.SenderUser=SenderUser;
        this.Recieveruser=Recieveruser;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){

            case SENDER_ITEM:
                SenderLayoutItemBinding senderLayoutItemBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.sender_layout_item,parent,false);
                return new SenderViewHolder(senderLayoutItemBinding);
            case RECIEVER_ITEM:
                RecieverLayoutItemBinding recieverLayoutItemBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                        ,R.layout.reciever_layout_item,parent,false);
                return new RecieverViewHolder(recieverLayoutItemBinding);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chats chats=chatsArrayList.get(position);
if(holder.getClass()==SenderViewHolder.class){

    ((SenderViewHolder) holder).senderLayoutItemBinding.setChats(chats);
    ((SenderViewHolder) holder).senderLayoutItemBinding.setUser(SenderUser);

}else{

    ((RecieverViewHolder)holder).recieverLayoutItemBinding.setChats(chats);
    ((RecieverViewHolder)holder).recieverLayoutItemBinding.setUser(Recieveruser);

}

    }

    @Override
    public int getItemCount() {
        return chatsArrayList.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
       private SenderLayoutItemBinding senderLayoutItemBinding;


        public SenderViewHolder(SenderLayoutItemBinding senderLayoutItemBinding) {
            super(senderLayoutItemBinding.getRoot());
            this.senderLayoutItemBinding=senderLayoutItemBinding;

        }

    }
    public class RecieverViewHolder extends RecyclerView.ViewHolder{
       private RecieverLayoutItemBinding recieverLayoutItemBinding;

        public RecieverViewHolder(RecieverLayoutItemBinding recieverLayoutItemBinding) {
            super(recieverLayoutItemBinding.getRoot());
            this.recieverLayoutItemBinding=recieverLayoutItemBinding;
        }
    }

    @Override
    public int getItemViewType(int position) {

        Chats messageModal=chatsArrayList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageModal.getSenderUID())){
            return SENDER_ITEM;
        }
        else{
            return RECIEVER_ITEM;
        }
    }
}
