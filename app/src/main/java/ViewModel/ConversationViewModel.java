package ViewModel;

import android.databinding.BaseObservable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Interface.FirebaseCallBacks;
import Interface.ModelCallBacks;
import Interface.Observer;
import Model.Chat;
import Model.MessageModel;
import Utility.FirebaseManager;
import Utility.Util;

public class ConversationViewModel extends BaseObservable implements FirebaseCallBacks, ModelCallBacks {

    private MessageModel messageModel;
    public ArrayList<Observer> observers;
    private String sender;
    private String receiver;
    private String path;


    public ConversationViewModel(String path, String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.path = path;
        messageModel = new MessageModel();
        observers = new ArrayList<>();
    }

    public void setListener() {
        List<FirebaseManager> instance = FirebaseManager.getInstance(path, sender, receiver, this);
        for (FirebaseManager firebaseManager : instance) {
            firebaseManager.addMessageListeners();
            break;
        }
    }

    public void sendMessage(String chatPath, String message, String sender, String receiver) {
        if (!message.trim().equals("")) {
            List<FirebaseManager> instance = FirebaseManager.getInstance(path, sender, chatPath, this);
            for (FirebaseManager firebaseManager : instance)
                firebaseManager.sendMessageToFirebase(message, sender, receiver);
        }
    }

    @Override
    public void onUpdate(DataSnapshot dataSnapshot) {
        messageModel.addMessages(dataSnapshot, this);
    }

    @Override
    public void onModelUpdated(ArrayList<Chat> messages) {
        if (messages.size() > 0) {
            notifyObservers(1, messages);
        }
    }


    public void addObserver(Observer client) {
        if (!observers.contains(client)) {
            observers.add(client);
        }
    }

    public void removeObserver(Observer clientToRemove) {
        if (observers.contains(clientToRemove)) {
            observers.remove(clientToRemove);
        }
    }

    public void notifyObservers(int eventType, ArrayList<Chat> messages) {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).onObserve(eventType, messages);
        }
    }

    public void onDestroy() {
        List<FirebaseManager> instance = FirebaseManager.getInstance(path, sender, receiver, this);
        for (FirebaseManager firebaseManager : instance) {
            firebaseManager.removeListeners();
            firebaseManager.destroy();
        }
    }

    public void uploadImage(byte[] image) {
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + sender + "_" + receiver + "/");
        UploadTask uploadTask = storageReference.putBytes(image);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                sendImage(path, sender, receiver, task.getResult().toString());
            }
        });
    }

    private void sendImage(String path, String sender, String receiver, String message) {
        if (!message.trim().equals("")) {
            List<FirebaseManager> instance = FirebaseManager.getInstance(path, sender, receiver, this);
            for (FirebaseManager firebaseManager : instance)
                firebaseManager.sendImageToFirebase(message, sender, receiver);
        }
    }
}

