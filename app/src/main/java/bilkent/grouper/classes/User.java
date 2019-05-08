package bilkent.grouper.classes;

import java.util.ArrayList;

public class User {

    // variables
    private String displayName;
    private String ID;
    private ArrayList<String> groups;
    private ArrayList<String> meetings;

    // constructors
    public User(){}

    public User(String displayName, String ID) {
        this.displayName = displayName;
        this.ID = ID;
    }

    public User(String displayName, String ID, ArrayList<String> groups, ArrayList<String> meetings) {
        this.displayName = displayName;
        this.ID = ID;
        this.groups = groups;
        this.meetings = meetings;
    }
// methods

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
