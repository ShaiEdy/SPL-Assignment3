package bgu.spl.net.api;

public class Customer {
    private String userName;
    private String password;
    private boolean isLoggedIn;

    public Customer() {
        this.userName = null;
        this.password = null;
        this.isLoggedIn = false;
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
}
