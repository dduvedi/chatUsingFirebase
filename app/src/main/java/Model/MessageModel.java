package Model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import Interface.ModelCallBacks;

public class MessageModel {
    private ArrayList<Chat> mMessages;

    public void addMessages(DataSnapshot dataSnapshot, ModelCallBacks callBacks) {
        if (mMessages == null) {
            mMessages = new ArrayList<>();
        }
        Chat chat = new Chat(dataSnapshot);
        mMessages.add(chat);
        callBacks.onModelUpdated(mMessages);
    }
}
