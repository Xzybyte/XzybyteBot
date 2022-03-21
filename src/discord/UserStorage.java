package discord;

import java.util.HashMap;

public class UserStorage {

    private HashMap<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void removeUser(User user) {
        users.remove(user.getId(), user);
    }

    public User getUserById(String id) {
        return users.get(id);
    }

}
