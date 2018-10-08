package Utility;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interface.FirebaseCallBacks;

public class FirebaseManager implements ChildEventListener {
    private volatile static List<FirebaseManager> list = null;
    private DatabaseReference databaseReference;
    private FirebaseCallBacks callBacks;

    public static synchronized List<FirebaseManager> getInstance(String path, String sender, String receiver, FirebaseCallBacks callBacks) {
        if (list == null || list.size() == 0) {
            synchronized (FirebaseManager.class) {
                list = new ArrayList<>();
                list.add(new FirebaseManager(path + sender + "_" + receiver, callBacks));
                list.add(new FirebaseManager(path + receiver + "_" + sender, callBacks));

            }
        }
        return list;
    }

    private FirebaseManager(String roomName, FirebaseCallBacks callBacks) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(roomName);
        this.callBacks = callBacks;
    }

    public void addMessageListeners() {
        databaseReference.addChildEventListener(this);
    }

    public void removeListeners() {
        databaseReference.removeEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        callBacks.onUpdate(dataSnapshot);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void sendMessageToFirebase(String message, String sender, String receiver) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("time", System.currentTimeMillis());
        map.put("sender", sender);
        map.put("receiver", receiver);
        map.put("isRead", false);

        String keyToPush = databaseReference.push().getKey();
        databaseReference.child(keyToPush).setValue(map);
    }

    public void sendImageToFirebase(String imageURL, String sender, String receiver) {
        Map<String, Object> map = new HashMap<>();
        map.put("imageURL", imageURL);
        map.put("time", System.currentTimeMillis());
        map.put("sender", sender);
        map.put("receiver", receiver);
        map.put("isRead", false);

        String keyToPush = databaseReference.push().getKey();
        databaseReference.child(keyToPush).setValue(map);
    }

    public void destroy() {
        list = null;
        callBacks = null;
    }
}
