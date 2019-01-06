package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

public class RegisterMessage extends Message {
    private String userName = "";
    private String password = "";


    public RegisterMessage(byte[] messageBytesArray) {
        super((short) 1);

        int index = 2; // represents the index we are currently looking at. starts from 2 because we dont care about the Opcode.

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
    public Message act(BidiMessagingProtocolImpl protocol) {
        DataBase dataBase = protocol.getDataBase();
        synchronized (dataBase.getUserNameToCustomer()) { /// sync to prevent tow users that register with same name in the same time
            if (dataBase.getUserNameToCustomer().containsKey(userName) || protocol.isLoggedIn() /*|| customer.isRegistered()*/) //if customer was already registered.
                return new ErrorMessage((short) 1);

            else { // if username is not already registered/
                Customer customer = new Customer();
                customer.setUserName(userName);
                customer.setPassword(password);
                dataBase.getUserNameToCustomer().put(userName, customer); // put it in the data map
                return new AckMessage((short) 1, null);
            }
        }
    }
}
