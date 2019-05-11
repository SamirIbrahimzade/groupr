package bilkent.grouper.classes;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Group {

    //properties
    private List<String> coordinatorIDs;
    private String groupName;
    private List<String> userIDs;
    private List<String> postIDs;
    private List<Meeting> meetings;
    private String photo;
    //constructors
    public Group(){
    }

    public Group(List<String> coordinatorIDs, String groupName, List<String> userIDs, List<String> postIDs, List<Meeting> meetings, String photo) {
        this.coordinatorIDs = coordinatorIDs;
        this.groupName = groupName;
        this.userIDs = userIDs;
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

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
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
}
