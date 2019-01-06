package bgu.spl.net.api;

import java.util.List;
import java.util.Vector;

public class Customer {
    //------------------fields--------------------------

    private String userName;
    private String password;
    private boolean isLoggedIn;
    private List<Customer> followingMe;
    private List<Customer> followedByMe;
    private List<String> posts;
    private List<String> PMs;
    private int connectionID;

    //------------------constructor----------------------

    public Customer() {
        this.isLoggedIn = false;
        this.followingMe = new Vector<>(); // todo- maybe other data structure ?
        this.followedByMe = new Vector<>(); // todo- maybe other data structure ?
        this.posts = new Vector<>();
        this.PMs = new Vector<>();
        this.connectionID = -1;
        this.userName="";
        this.password="";
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

    public List<Customer> getFollowingMe() {
        return followingMe;
    }

    public List<Customer> getFollowedByMe() {
        return followedByMe;
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

    public void setConnectionID(int connectionID) {
        this.connectionID = connectionID;
    }

    //-------------others---------------------------------

    public void addFollower(Customer customer) {
        followingMe.add(customer);
    }

    public void removeFollower(Customer customer) {
        followingMe.remove(customer);
    }

    public int getNumOfFollowers(){
        return followingMe.size();
    }

    public void addFollowing(Customer customer) {
        followedByMe.add(customer);
    }

    public void removeFollowing(Customer customer) {
        followedByMe.remove(customer);
    }

    public int getNumOfFollowing(){
        return followedByMe.size();
    }

    public void addPM(String content) {
        PMs.add(content); //todo think if content is enough or you want also the sender
    }

    public void addPost(String content) {
        posts.add(content);
    }

    public int getNumOfPost (){
        return posts.size();
    }
}
