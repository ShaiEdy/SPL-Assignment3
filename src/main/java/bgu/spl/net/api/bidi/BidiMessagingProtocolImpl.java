package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.impl.messages.*;
import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {
    private int connectionId;
    private Connections connections;
    private boolean shouldTerminate;
    private DataBase dataBase;
    //private Customer customer; //this object will never be initialized in the protocol. only in RegisterMessage.
    private boolean isLoggedIn = false;
    private String userName = "";


    public BidiMessagingProtocolImpl(DataBase dataBase) {
        this.dataBase = dataBase;
        this.shouldTerminate = false;
        ///this.customer = new Customer();
    }
    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        //this.customer.setConnectionID(connectionId);
    }

    @Override
    public void process(Message message) {
        // get a specific message that was created in encoder decoder
        //we use here the act messages of the messages for doing the specific process that needed
        //we will send back a response using connections.
        Message newMessage= message.act(this);
        connections.send(connectionId,newMessage); // send ack/ error to the client

        //--------dealing with notification has to be send to list of customers------//
        Pair<NotificationMessage, Vector<Customer> > notificationAndUsersVector= dataBase.getUserNameToNotificationSendList().get(userName);
        if (notificationAndUsersVector!=null){ // the pair is exist (I caused notification)
            Vector<Customer> usersVector = notificationAndUsersVector.getValue(); // second
            NotificationMessage notificationMessage= notificationAndUsersVector.getKey(); // first
            for (Customer otherUser: usersVector){
                if (otherUser.isLoggedIn()) {
                    connections.send(otherUser.getConnectionID(), notificationMessage); //send notification that needed to be send
                }
                else{
                    if (dataBase.getNotificationsToBeSendInLogin().get(otherUser.getUserName())==null){
                        dataBase.getNotificationsToBeSendInLogin().put(otherUser.getUserName(), new Vector<NotificationMessage>());
                    }
                    dataBase.getNotificationsToBeSendInLogin().get(otherUser.getUserName()).add(notificationMessage);
                    //for un-logeIn customer, we add this notification to the list of notification to be send in the future
                }
              }
            dataBase.getUserNameToNotificationSendList().remove(userName); //delete from the hash map the customer that sent notification and we already deal with, remove the key and the value (pair)
        }
        //----dealing with notification "wait" to be send to a client that log in---//
        if (message instanceof LogInMessage) {
            Vector<NotificationMessage> notificationMessageVector = dataBase.getNotificationsToBeSendInLogin().get(userName);
            if (notificationMessageVector != null && !notificationMessageVector.isEmpty()) {
                //mean this client has notification "waiting" to be send to him
                for (NotificationMessage notificationMessage : notificationMessageVector) {
                    connections.send(connectionId, notificationMessage);
                    //send the customer all the needed notification that waited
                }
                notificationMessageVector.clear();
            }
        }
        //-----------case of logOut- dealing with terminate the connection--------------//
        if (message instanceof LogOutMessage){
            if (newMessage instanceof AckMessage)shouldTerminate=true;
            //only if logout succeed (we send the client log out ack) so we terminate
        }
    }

    /**
     * @return true if the connection should be terminated
     */
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    /**
     * @param byteArr- array of bytes represents the message
     * @return short represent by the two first byte of the array
     */
    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public Connections getConnections() {
        return connections;
    }
}
