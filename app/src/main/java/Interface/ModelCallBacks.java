package Interface;

import java.util.ArrayList;

import Model.Chat;

public interface ModelCallBacks {
    void onModelUpdated(ArrayList<Chat> messages);
}
