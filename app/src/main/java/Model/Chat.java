package Model;

import android.databinding.BaseObservable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Chat extends BaseObservable {
    @Exclude
    private String conversationType;
    public String sender;
    public String receiver;
    public String message;
    public Boolean read;
    public String imageURL;

    public Chat() {
    }

    public Chat(DataSnapshot dataSnapshot) {
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
        this.message = object.get("message") == null ? null : object.get("message").toString();
        this.imageURL = object.get("imageURL") == null ? null : object.get("imageURL").toString();
        this.sender = object.get("sender").toString();
        this.receiver = object.get("receiver").toString();
        this.read = (Boolean) object.get("isRead");
        this.conversationType = dataSnapshot.getRef().getParent().getKey();
    }


    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getMessage() {
        return message;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConversationType() {
        return conversationType;
    }
}
