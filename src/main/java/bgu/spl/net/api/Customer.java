package bgu.spl.net.api;

public class Customer {
    private String userName;
    private String password;
    private boolean login;

    public Customer(String userName, String password) {
        this.userName = userName;
        this.password = password;
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

    public void setLogin(boolean login) {
        this.login = login;
    }
}
