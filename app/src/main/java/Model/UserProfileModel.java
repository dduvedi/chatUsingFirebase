package Model;

public class UserProfileModel extends UserRegisterModel {
    private String id;

    public UserProfileModel() {
    }

    public UserProfileModel(String id, String firstName, String lastName, String userName, String userImage) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setUserName(userName);
        super.setUserImage(userImage);
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
