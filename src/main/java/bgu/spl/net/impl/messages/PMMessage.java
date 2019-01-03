package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class PMMessage extends Message {
    String userNameToSendPM;
    String content;

    public PMMessage(byte [] bytes) {
        int index=2;
        while (bytes[index]!='\0'){
            userNameToSendPM += Byte.toString((bytes[index]));  // we append the userName with the next byte.
            index++;
        }
        index++; //pass the \0 byte
        while (bytes[index]!='\0'){
            content += Byte.toString((bytes[index]));  // we append the content with the next byte.
            index++;
        }
    }


    @Override
    public Message act(DataBase dataBase, Customer customer) {
        if (!customer.isLoggedIn()|| dataBase.getUserNameToCustomer().get(userNameToSendPM)==null)
            return new ErrorMessage((short) 6); //if the sender is not logged in or the receiver is unRegistered we send error
        else {
            String senderUserName = customer.getUserName();
            dataBase.getUserNameToCustomer().get(userNameToSendPM).addPM(content); //add to the receiver
            customer.addPM(content); // add to the sender //todo think if content or this
            NotificationMessage notificationMessage = new NotificationMessage((byte) 0, senderUserName, content);
            Vector<Customer> vectorOfReceivers = new Vector<>();
            Customer receivedCustomer = dataBase.getUserNameToCustomer().get(userNameToSendPM);
            vectorOfReceivers.add(receivedCustomer);
            Pair<NotificationMessage, Vector<Customer>> pair = new Pair<>(notificationMessage, vectorOfReceivers);
            dataBase.getUserNameToNotificationSendList().put(senderUserName, pair);
            /// all this for creating notification that will be send by process of protocol
            return new AckMessage((short) 6, null);
        }
    }
}
