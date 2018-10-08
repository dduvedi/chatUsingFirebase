package ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import Model.UserRegisterModel;

public class UserListViewModel extends ViewModel {

    MutableLiveData<UserRegisterModel> registeredUser = new MutableLiveData<>();


    public void fetchRegisteredUser() {
    }

    public LiveData<UserRegisterModel> getUserList() {
        return registeredUser;
    }
}
