package com.groupr.groupr;

import java.security.acl.Group;
import java.util.ArrayList;

public class Groups {

    //properties
    private int id;
    private ArrayList<User> users;

    //constructor
    Groups(int id){
        users = new ArrayList<>(0);
        id = this.id;
    }
    public int setId(int id){
        this.id = id;
        return id;
    }
    public int getId(){
        return id;
    }
    public void addUser(User user){
        users.add(user);
    }
    public void removeUser(User user){
        users.remove(user);
    }

}
