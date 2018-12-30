package bgu.spl.net.api;

import java.util.List;
import java.util.Vector;

public class Customer {
    private String userName;
    private String password;
    private boolean isLoggedIn;
    private List folloing ;

    public Customer(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.isLoggedIn = false;
        this.folloing= new Vector(); // todo- maybe other data structure ?
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

    public void addFolloing(Customer customer){
        folloing.add(customer);
    }

    public void removeFolloing(Customer customer){
        folloing.remove(customer);
    }
}
