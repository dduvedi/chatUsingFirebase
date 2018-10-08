package ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import Model.UserRegisterModel;
import Utility.Util;

public class UserRegisterViewModel extends ViewModel {
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> userName = new MutableLiveData<>();

    public MutableLiveData<Drawable> userImage = new MutableLiveData<>();
    public MutableLiveData<Boolean> showProgress = new MutableLiveData<>();

    private MutableLiveData<UserRegisterModel> userMutableLiveData;

    public MutableLiveData<Action> actionSelected = new MutableLiveData<>();

    public LiveData<UserRegisterModel> getSignUpResponse() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }

    public LiveData<Boolean> showProgressDialog() {
        return showProgress;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void onSignUpClicked() {
        UserRegisterModel model = new UserRegisterModel();
        model.setFirstName(firstName.getValue());
        model.setLastName(lastName.getValue());
        model.setUserName(userName.getValue());

        if (model.isFirstNameValid()) {
            errorMessage.setValue("First name cannot be empty");
            return;
        }
        if (model.isUserNameValid()) {
            errorMessage.setValue("User name cannot be empty");
            return;
        }
        showProgress.setValue(true);

        checkIfUserNameExists(model);

    }

    private void checkIfUserNameExists(final UserRegisterModel model) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.orderByChild("userName").equalTo(model.getUserName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showProgress.setValue(false);
                    errorMessage.setValue("User exists with this user name");
                    return;
                } else {
                    if (userImage.getValue() != null) {
                        uploadImage(model);
                    } else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference userRef = reference.child("users");
                        userRef.push().setValue(model).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //add slack api to get real tim exception message
                                userMutableLiveData.setValue(null);
                            }
                        });
                        userMutableLiveData.setValue(model);
                        showProgress.setValue(false);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showProgress.setValue(false);
                errorMessage.setValue(databaseError.getMessage());
            }
        });
    }

    private void uploadImage(final UserRegisterModel model) {
        if (model.isFirstNameValid()) {
            errorMessage.setValue("First name cannot be empty");
            return;
        }
        if (model.isUserNameValid()) {
            errorMessage.setValue("User name cannot be empty");
            return;
        }
        showProgress.setValue(true);

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + userName.getValue());
        UploadTask uploadTask = storageReference.putBytes(Util.bitmapToByteArray(((BitmapDrawable) userImage.getValue()).getBitmap()));

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    errorMessage.setValue("Error while uploading file");
                    return;
                }
                model.setUserImage(task.getResult().toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userRef = reference.child("users");
                userRef.push().setValue(model).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userMutableLiveData.setValue(null);
                    }
                });
                userMutableLiveData.setValue(model);
                showProgress.setValue(false);
            }
        });
    }

    public LiveData<Action> getActionClicked() {
        return actionSelected;
    }

    public void onLoginClick() {
        actionSelected.setValue(Action.LOGIN_CLICKED);
    }

    public enum Action {
        LOGIN_CLICKED,
        IMAGE_CLICK;
    }

    public void onImageClick() {
        actionSelected.setValue(Action.IMAGE_CLICK);
    }
}
