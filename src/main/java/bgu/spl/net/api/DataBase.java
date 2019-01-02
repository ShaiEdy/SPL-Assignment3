package bgu.spl.net.api;

import bgu.spl.net.impl.messages.NotificationMessage;
import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    private ConcurrentHashMap<String, Customer> userNameToCustomer;
    //This dataMap is from each userName that was registered to a specific Customer Object.
    private ConcurrentHashMap<String, Pair<NotificationMessage, Vector<Customer>>> userNameToNotificationSendList;
    //This dataMap is from a userName of the sender (of the Post or PM) to the list of customers that should receive the Notification.
    private ConcurrentHashMap<String, Vector<NotificationMessage>> notificationsToBeSendInLogin;
    public DataBase() {
        userNameToCustomer = new ConcurrentHashMap<>();
        userNameToNotificationSendList = new ConcurrentHashMap<>();
        notificationsToBeSendInLogin= new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, Customer> getUserNameToCustomer() {
        return userNameToCustomer;
    }

    public ConcurrentHashMap<String, Pair<NotificationMessage, Vector<Customer>>> getUserNameToNotificationSendList() {
        return userNameToNotificationSendList;
    }
}
