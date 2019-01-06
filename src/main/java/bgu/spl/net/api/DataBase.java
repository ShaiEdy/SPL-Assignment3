package bgu.spl.net.api;

import bgu.spl.net.impl.messages.NotificationMessage;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    private ConcurrentHashMap<String, Customer> userNameToCustomer;
    //This dataMap is from each userName that was registered to a specific Customer Object.

    private ConcurrentHashMap<String, Vector<NotificationMessage>> notificationsToBeSendInLogin;
    public DataBase() {
        userNameToCustomer = new ConcurrentHashMap<>();
        notificationsToBeSendInLogin= new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, Customer> getUserNameToCustomer() {
        return userNameToCustomer;
    }

    public ConcurrentHashMap<String, Vector<NotificationMessage>> getNotificationsToBeSendInLogin() {
        return notificationsToBeSendInLogin;
    }
}
