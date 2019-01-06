package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class LogInMessage extends Message {
    private short opcode= 2;
    private int arrayLength;
    private int index = 2; // represents the index we are currently looking at. starts from 2 because we dont care about the Opcode.
    private String userName = "";
    private String password = "";

    public LogInMessage(byte[] messageBytesArray) {
        super((short) 2);

        this.arrayLength = messageBytesArray.length;

        userName = appendToString(messageBytesArray, index);
        index = index + userName.length() + 1;
        password = appendToString(messageBytesArray, index);

    }


    /**
     * Used to make a String of all the bytes from messageBytesArray[index] to the first /0 in messageBytesArray.
     **/
    private String appendToString(byte[] messageBytesArray, int index) {
        String toReturn = "";
        byte[] wordInByte;
        int counter = 0;
        int primaryIndex = index;
        while (messageBytesArray[index] != '\0') {
            counter++;
            index++;
        }
        wordInByte = new byte[counter];
        index = primaryIndex;

        for (int i = 0; i < counter; i++) {
            wordInByte[i] = messageBytesArray[index];
            index++;
        }
        return new String(wordInByte);
    }

    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        DataBase dataBase = protocol.getDataBase();
        if (dataBase.getUserNameToCustomer().containsKey(userName)) { //if customer is registered.
            if (!protocol.isLoggedIn()) { // if the protocol customer is not logged in already
                Customer customerToLogIn = dataBase.getUserNameToCustomer().get(userName); // we get the customer that we should log in. notice that customer in the signature is not necesserily the actual customer and might be empty
                if (!customerToLogIn.isLoggedIn() && customerToLogIn.getPassword().equals(password)) { // if other client didn't already logged in to it and the password is fine.
                    synchronized (customerToLogIn) {
                        protocol.setLoggedIn(true);
                        customerToLogIn.setLoggedIn(true);
                        protocol.setUserName(customerToLogIn.getUserName());
                        customerToLogIn.setConnectionID(protocol.getConnectionId());

                        Vector<NotificationMessage> notificationMessageVector = dataBase.getNotificationsToBeSendInLogin().get(userName);
                        if (notificationMessageVector != null && !notificationMessageVector.isEmpty()) {
                            //mean this client has notification "waiting" to be send to him
                            for (NotificationMessage notificationMessage : notificationMessageVector) {
                                protocol.getConnections().send(protocol.getConnectionId(), notificationMessage);
                                //send the customer all the needed notification that waited
                            }
                            notificationMessageVector.clear();
                        }
                    }
                    return new AckMessage((short) 2, null);
                }
            }
        }
        return new ErrorMessage((short) 2);
    }
}
