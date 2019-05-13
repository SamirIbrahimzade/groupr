package bilkent.grouper.classes;

import java.util.List;

public class Group {

    //properties
    private List<String> coordinatorIDs;
    private String groupName;
    private List<User> users;
    private List<String> postIDs;
    private List<Meeting> meetings;
    private String photo;
    //constructors
    public Group(){
    }

    public Group(List<String> coordinatorIDs, String groupName, List<User> users, List<String> postIDs, List<Meeting> meetings, String photo) {
        this.coordinatorIDs = coordinatorIDs;
        this.groupName = groupName;
        this.users = users;
        this.postIDs = postIDs;
        this.meetings = meetings;
        this.photo = photo;
    }

    public List<String> getCoordinatorIDs() {
        return coordinatorIDs;
    }

    public void setCoordinatorIDs(List<String> coordinatorIDs) {
        this.coordinatorIDs = coordinatorIDs;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<String> getPostIDs() {
        return postIDs;
    }

    public void setPostIDs(List<String> postIDs) {
        this.postIDs = postIDs;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void addUser(User user){
        users.add(user);
    }
}
