package bgu.spl.net.api;

import java.util.List;
import java.util.Vector;

public class Customer {
    private String userName;
    private String password;
    private boolean isLoggedIn;
    private List following;

    public Customer(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.isLoggedIn = false;
        this.following= new Vector(); // todo- maybe other data structure ?
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLogin() {
        return isLoggedIn;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoggedInStatus(boolean login) {
        this.isLoggedIn = login;
    }

    public void addFollowing(Customer customer){
        following.add(customer);
    }

    public void removeFollowing(Customer customer){
        following.remove(customer);
    }
}
