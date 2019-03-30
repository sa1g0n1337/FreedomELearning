package freedom.com.freedom_e_learning.model;

public class User {

    private String Uid;
    private String Name;
    private String Email;
    private String ProfilePicture;

    public User() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.ProfilePicture = profilePicture;
    }
}
