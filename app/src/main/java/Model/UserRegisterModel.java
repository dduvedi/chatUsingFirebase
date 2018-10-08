package Model;

import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

public class UserRegisterModel {

    private String firstName;

    private String lastName;

    private String userName;

    private String userImage;

    public UserRegisterModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Exclude
    public boolean isFirstNameValid() {
        return TextUtils.isEmpty(firstName);
    }

    @Exclude
    public boolean isUserNameValid() {
        return TextUtils.isEmpty(userName);
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }


}
