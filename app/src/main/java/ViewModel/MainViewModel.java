package ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.UserCollection;
import Model.UserCollectionDetails;
import Model.UserRegisterModel;

public class MainViewModel extends ViewModel {

    public int itemPerPage = 18;

    private String lastItem;

    MutableLiveData<UserCollection> userCollectionResponse = new MutableLiveData<>();

    MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public MutableLiveData<Boolean> showProgress = new MutableLiveData<>();


    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<UserCollection> fetchAllUserList() {
        showProgress.setValue(true);
        Query users;
        if (lastItem == null || lastItem.isEmpty()) {
            users = FirebaseDatabase.getInstance().getReference().child("users").limitToFirst(itemPerPage);
        } else {
            users = FirebaseDatabase.getInstance().getReference().child("users").startAt(lastItem).limitToFirst(itemPerPage);
        }


        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showProgress.setValue(false);
                    UserCollection userCollection = new UserCollection();
                    List<UserCollectionDetails> userCollectionDetails = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        lastItem = ds.getKey();
                        UserRegisterModel userRegisterModel = ds.getValue(UserRegisterModel.class);
                        UserCollectionDetails collectionDetails = new UserCollectionDetails();
                        collectionDetails.setUserRegisterModel(userRegisterModel);
                        userCollectionDetails.add(collectionDetails);
                    }
                    userCollection.setUserCollectionDetailsList(userCollectionDetails);
                    userCollectionResponse.setValue(userCollection);
                } else {
                    if (lastItem == null && lastItem.isEmpty())
                        errorMessage.setValue("No users found");

                    showProgress.setValue(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showProgress.setValue(false);
                errorMessage.setValue(databaseError.getMessage());
            }
        });

        return userCollectionResponse;
    }


    public LiveData<Boolean> showProgressDialog() {
        return showProgress;
    }

    public void loadMoreData() {
        fetchAllUserList();
    }
}
