package bgu.spl.net.api;

public class Customer {
    private String userName;
    private String password;
    private boolean login;

    public Customer() {
        this.userName = null;
        this.password = null;
        this.login = false;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLogin() {
        return login;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
