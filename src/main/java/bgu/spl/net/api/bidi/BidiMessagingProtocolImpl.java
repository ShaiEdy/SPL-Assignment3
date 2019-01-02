package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.impl.messages.NotificationMessage;
import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    private int connectionId;
    private Connections connections;
    private boolean shouldTerminate;
    private DataBase dataBase;
    private Customer customer; //this object will never be initialized in the protocol. only in RegisterMessage.


    public BidiMessagingProtocolImpl(DataBase dataBase) {
        this.dataBase = dataBase;
        this.shouldTerminate = false;
        this.customer = new Customer();
    }
    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        this.customer.setConnectionID(connectionId);
    }

    @Override
    public void process(T message) {// get a specific message that was created in encoder decoder
        //use here the act messages of the messages for doing the specific process that needed
        //we will send back a response using connections.

        // dealing with notification has to be send to list of customers
        Message newMessage= ((Message)message).act(dataBase,customer);
        connections.send(connectionId,newMessage); // send ack/ error to the client
        Pair<NotificationMessage, Vector<Customer> > notificationAndUsersVector= dataBase.getUserNameToNotificationSendList().get(customer.getUserName());
        if (notificationAndUsersVector!=null){ // the pair is exist (I caused notification)
            Vector<Customer> usersVector = notificationAndUsersVector.getValue(); // second
            NotificationMessage notificationMessage= notificationAndUsersVector.getKey(); // first
            for (Customer otherUser: usersVector){
                if (otherUser.isLoggedIn()) {
                    connections.send(otherUser.getConnectionID(), notificationMessage); //send notification that needed to be send
                }
                else{
                    dataBase.getNotificationsToBeSendInLogin().get(otherUser.getUserName()).add(notificationMessage);
                    //for un-logeIn customer, we add this notification to the list of notification to be send in the future
                }
              }
            dataBase.getUserNameToNotificationSendList().remove(customer.getUserName()); //delete from the hash map the customer that sent notification and we already deal with, remove the key and the value (pair)

        }
        Vector<NotificationMessage> notificationMessageVector= dataBase.getNotificationsToBeSendInLogin().get(customer.getUserName());
        if (notificationMessageVector!=null &&!notificationMessageVector.isEmpty()){
        //mean this client has notification "waiting" to be send to him
            for (NotificationMessage notificationMessage: notificationMessageVector){
                connections.send(customer.getConnectionID(),notificationMessage);
                //send the customer all the needed notification that waited
            }
            notificationMessageVector.clear();
        }
        dataBase.getUserNameToNotificationSendList().
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
}
