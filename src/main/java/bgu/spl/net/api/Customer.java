package bgu.spl.net.api;

import java.util.List;
import java.util.Vector;

public class Customer {
    //------------------fields--------------------------

    private String userName;
    private String password;
    private boolean isLoggedIn;
    private List<Customer> following;
    private List<String> posts;
    private List<String> PMs;
    private int connectionID;

    //------------------constructor----------------------

    public Customer() {
        this.isLoggedIn = false;
        this.following= new Vector<>(); // todo- maybe other data structure ?
        this.posts = new Vector<>();
        this.PMs = new Vector<>();
        this.connectionID = -1;
    }

    //------------------getters--------------------------

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public List<Customer> getFollowing() {
        return following;
    }

    public List<String> getPosts() {
        return posts;
    }

    public List<String> getPMs() {
        return PMs;
    }

    public int getConnectionID() {
        return connectionID;
    }

    //------------------setters--------------------------

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setFollowing(List<Customer> following) {
        this.following = following;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    public void setPMs(List<String> PMs) {
        this.PMs = PMs;
    }

    public void setConnectionID(int connectionID) {
        this.connectionID = connectionID;
    }
}
