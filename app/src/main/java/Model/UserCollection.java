package Model;

import java.util.List;

public class UserCollection {

    private List<UserCollectionDetails> userCollectionDetailsList;

    public List<UserCollectionDetails> getUserCollectionDetailsList() {
        return userCollectionDetailsList;
    }

    public void setUserCollectionDetailsList(List<UserCollectionDetails> userCollectionDetailsList) {
        this.userCollectionDetailsList = userCollectionDetailsList;
    }
}
