package Model;

import android.text.TextUtils;

public class UserLoginModel {

    private String userName;


    public UserLoginModel() {
    }

    public UserLoginModel(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isUserNameValid() {
        return TextUtils.isEmpty(userName);
    }
}
