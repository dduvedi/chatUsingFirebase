package Model;

public class UserCollectionDetails {

    private UserRegisterModel userRegisterModel;
    private int unreadMessage;

    public UserRegisterModel getUserRegisterModel() {
        return userRegisterModel;
    }

    public void setUserRegisterModel(UserRegisterModel userRegisterModel) {
        this.userRegisterModel = userRegisterModel;
    }

    public int getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(int unreadMessage) {
        this.unreadMessage = unreadMessage;
    }
}
