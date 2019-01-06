package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import java.util.Vector;

public class PMMessage extends Message {
    private byte[] userNameBytes;
    private byte[] contentBytes;
    private String userNameToSendPM;
    private String content;

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
    @SuppressWarnings("unchecked")
    public Message act(BidiMessagingProtocolImpl protocol) {
        DataBase dataBase = protocol.getDataBase();

        if (!protocol.isLoggedIn()|| dataBase.getUserNameToCustomer().get(userNameToSendPM)==null)
            return new ErrorMessage((short) 6); //if the sender is not logged in or the receiver is unRegistered we send error
        else {
            String thisCustomerUserName = protocol.getUserName();
            Customer thisCustomer = dataBase.getUserNameToCustomer().get(thisCustomerUserName);

            dataBase.getUserNameToCustomer().get(userNameToSendPM).addPM(content); //add to the receiver
            thisCustomer.addPM(content); // add to the sender
            NotificationMessage notificationMessage = new NotificationMessage((byte) 0, thisCustomerUserName, content);
            Customer receivedCustomer = dataBase.getUserNameToCustomer().get(userNameToSendPM);
            synchronized (receivedCustomer) {
                if (receivedCustomer.isLoggedIn())
                    protocol.getConnections().send(receivedCustomer.getConnectionID(), notificationMessage);
                else {
                    if (dataBase.getNotificationsToBeSendInLogin().get(receivedCustomer.getUserName()) == null) {
                        dataBase.getNotificationsToBeSendInLogin().put(receivedCustomer.getUserName(), new Vector<NotificationMessage>());
                    }
                    dataBase.getNotificationsToBeSendInLogin().get(receivedCustomer.getUserName()).add(notificationMessage);
                }
            }
            return new AckMessage((short) 6, null);
        }
    }
}
