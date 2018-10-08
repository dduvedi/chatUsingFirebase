package ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.UserLoginModel;
import Model.UserProfileModel;

public class UserLoginViewModel extends ViewModel {
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MutableLiveData<UserProfileModel> userMutableLiveData;
    public MutableLiveData<Action> actionSelected = new MutableLiveData<>();
    public MutableLiveData<Boolean> showProgress = new MutableLiveData<>();

    public LiveData<UserProfileModel> getSignInResponse() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }


    public LiveData<Boolean> showProgressDialog() {
        return showProgress;
    }

    public void onLoginClicked() {
        UserLoginModel model = new UserLoginModel(userName.getValue());

        if (model.isUserNameValid()) {
            errorMessage.setValue("User name cannot be empty");
            showProgress.setValue(false);
            return;
        }
        showProgress.setValue(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.orderByChild("userName").equalTo(model.getUserName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        showProgress.setValue(false);
                        UserProfileModel value = ds.getValue(UserProfileModel.class);
                        UserProfileModel details = new UserProfileModel(ds.getKey(), value.getFirstName(), value.getLastName(),
                                value.getUserName(), value.getUserImage());
                        userMutableLiveData.setValue(details);
                        break;
                    }
                } else {
                    showProgress.setValue(false);
                    userMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showProgress.setValue(false);
                errorMessage.setValue(databaseError.getMessage());
            }
        });
    }

    public void onSignUpClicked() {
        actionSelected.setValue(Action.LOGIN_CLICKED);
    }

    public LiveData<Action> getActionClicked() {
        return actionSelected;
    }

    public enum Action {
        LOGIN_CLICKED;
    }

}
