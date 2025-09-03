import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManager {
    private final List<User> users;

    public UserManager() {
        this.users = new ArrayList<>();
    }

    public void registerUser(User user) { // simple add, no prints
        users.add(user);
    }

    public User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.authenticate(password)) {
                return u;
            }
        }
        return null;
    }
    // getters
    public List<User> getUsers() {
        return users;
    }

    // expose read-only views to GUI
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<Donor> getAllDonors() { 
        return users.stream().filter(u -> u instanceof Donor).map(u -> (Donor) u).collect(Collectors.toList());
    }

    public List<Recipient> getAllRecipients() { 
        return users.stream().filter(u -> u instanceof Recipient).map(u -> (Recipient) u).collect(Collectors.toList());
    }
}

