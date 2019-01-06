package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import java.util.Vector;

public class LogInMessage extends Message {
    private int index = 2; // represents the index we are currently looking at. starts from 2 because we dont care about the Opcode.
    private String userName = "";
    private String password = "";

    public LogInMessage(byte[] messageBytesArray) {
        super((short) 2);

        userName = appendToString(messageBytesArray, index);
        index = index + userName.length() + 1;
        password = appendToString(messageBytesArray, index);

    }


    /**
     * Used to make a String of all the bytes from messageBytesArray[index] to the first /0 in messageBytesArray.
     **/
    private String appendToString(byte[] messageBytesArray, int index) {
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
    @SuppressWarnings("unchecked")
    public Message act(BidiMessagingProtocolImpl protocol) {
        DataBase dataBase = protocol.getDataBase();
        if (dataBase.getUserNameToCustomer().containsKey(userName)) { //if customer is registered.
            if (!protocol.isLoggedIn()) { // if the protocol customer is not logged in already
                Customer customerToLogIn = dataBase.getUserNameToCustomer().get(userName); // we get the customer that we should log in. notice that customer in the signature is not necesserily the actual customer and might be empty
                synchronized (customerToLogIn) { // sync for preventing post/pm to be sent while customer is log in and preventing 2 login in the same time.
                    if (!customerToLogIn.isLoggedIn() && customerToLogIn.getPassword().equals(password)) { // if other client didn't already logged in to it and the password is fine.
                        //----dealing with notification "wait" to be send to a client that log in---//
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
                        return new AckMessage((short) 2, null);
                    }
                }
            }
        }
        return new ErrorMessage((short) 2);
    }
}
