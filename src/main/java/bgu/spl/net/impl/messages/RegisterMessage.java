package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import java.util.concurrent.ConcurrentHashMap;

public class RegisterMessage extends Message {
    private short opcode= 1;
    private int arrayLength;
    private String userName = "";
    private String password = "";


    public RegisterMessage(byte[] messageBytesArray) {
        super((short)1);

        this.arrayLength = messageBytesArray.length;
        int index = 2; // represents the index we are currently looking at. starts from 2 because we dont care about the Opcode.

        userName = appendToString(messageBytesArray, index);
        index= index +userName.length()+1;
        password = appendToString(messageBytesArray, index);
    }

    /**
     * Used to make a String of all the bytes from messageBytesArray[index] to the first /0 in messageBytesArray.
     **/
    private String appendToString(byte[] messageBytesArray, int index) {
        String toReturn ="";
        byte[] wordInByte;
        int counter=0;
        int primaryIndex= index;
        while (messageBytesArray[index] != '\0') {
            counter++;
            index++;
        }
        wordInByte= new byte[counter];
        index= primaryIndex;

        for (int i = 0; i < counter; i++) {
            wordInByte[i]= messageBytesArray[index];
            index++;
        }
        return new String(wordInByte);
    }

    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
            DataBase dataBase = protocol.getDataBase();
            //Customer customer = protocol.getCustomer();
            //first we will check that this customer is not already registered.
            synchronized (dataBase.getUserNameToCustomer()) { // sync to prevent tow users that register with same name in the same time
            if (dataBase.getUserNameToCustomer().containsKey(userName) || protocol.isLoggedIn() /*|| customer.isRegistered()*/) //if customer was already registered.
                return new ErrorMessage((short) 1); //todo: think if we handle correctly situation of two customers registering after each other.

            else { // if username is not already registered
                Customer customer = new Customer();
                customer.setUserName(userName);
                customer.setPassword(password);
                customer.setRegistered(true);
                dataBase.getUserNameToCustomer().put(userName, customer); // put it in the data map
                //todo: do we need to think about problems caused by multiThreading? what if two clients register at the same time with the same user name?
                return new AckMessage((short) 1, null);
            }
        }
    }
}
