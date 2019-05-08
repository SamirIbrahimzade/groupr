package bilkent.grouper.classes;

public class User {

    // variables
    private String displayName;


    // constructors
    public User(){}

    public User(String displayName) {
        this.displayName = displayName;
    }

    // methods

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
