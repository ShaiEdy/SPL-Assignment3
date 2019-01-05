package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class PMMessage extends Message {
    private short opcode= 6;
    byte[] userNameBytes;
    byte[] contentBytes;
    String userNameToSendPM;
    String content;

    public PMMessage(byte [] bytes) {
        super((short)6);
        int index=2; // index of the input bytes array
        int userNameCounter=0;
        int contentCounter=0;
        int indexOfFirstLetter= index;
        while (bytes[index]!='\0'){ // counting num of bytes for user name
            userNameCounter++;
            index++;
        }
        userNameBytes= new byte[userNameCounter]; // creating array bytes for user name
        for (int i = 0; i <userNameCounter ; i++) {
            userNameBytes[i]= bytes[indexOfFirstLetter];
            indexOfFirstLetter++;
        }
        userNameToSendPM= new String(userNameBytes);

        index++; //pass the \0 byte
        indexOfFirstLetter++;
        while (bytes[index]!='\0'){ // counting the bytes of content
            contentCounter++;
            index++;
        }
        contentBytes= new byte[contentCounter];
        for (int i = 0; i <contentCounter ; i++) {
            contentBytes[i]= bytes[indexOfFirstLetter];
            indexOfFirstLetter++;
        }

        content= new String(contentBytes);

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
        // todo change to byte
        }
    }
}
