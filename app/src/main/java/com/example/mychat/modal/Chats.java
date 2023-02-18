package com.example.mychat.modal;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.Objects;

public class Chats extends BaseObservable {


    private String message;
    private String senderUID;




    private long timestamp;

    public Chats() {
    }

    public Chats(String message, String senderUID, long timestamp) {
        this.message = message;
        this.senderUID = senderUID;
        this.timestamp = timestamp;
    }


    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    @Bindable
    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
        notifyPropertyChanged(BR.senderUID);
    }

    @Bindable
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        notifyPropertyChanged(BR.timestamp);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chats chats = (Chats) o;
        return timestamp == chats.timestamp && message.equals(chats.message) && senderUID.equals(chats.senderUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, senderUID, timestamp);
    }
}
