package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

import java.util.concurrent.ConcurrentHashMap;

public class LogInMessage extends Message {
    private int arrayLength;
    private int index = 2; // represents the index we are currently looking at. starts from 2 because we dont care about the Opcode.
    private String userName = "";
    private String password = "";

    public LogInMessage(byte[] messageBytesArray) {
        this.arrayLength = messageBytesArray.length;

        appendToString(messageBytesArray, userName);
        index++;
        appendToString(messageBytesArray, password);

    }


    /**
     * Used to make a String of all the bytes from messageBytesArray[index] to the first /0 in messageBytesArray.
     **/
    private void appendToString(byte[] messageBytesArray, String stringToAppendTo) {
        while (messageBytesArray[index] != '\0') {
            stringToAppendTo += Byte.toString(messageBytesArray[index]); // we append the userName with the next byte.
            index++;
        }
    }

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer) {
        if (dataMap.containsKey(userName)) { //if customer is registered.
            if (!customer.isLoggedIn()) { // if the protocol customer is not logged in already
                int connectionID = customer.getConnectionID(); // we want to keep the old connectionID and to not lose it.
                Customer customerToLogIn = dataMap.get(userName); // we get the customer that we should log in. notice that customer in the signature is not necesserily the actual customer and might be empty
                if (!customerToLogIn.isLoggedIn() && customerToLogIn.getPassword().equals(password)) { // if other client didn't already logged in to it and the password is fine.
                    customer = customerToLogIn; // we want the protocol's customer to be the actual one that is going to be logged in.
                    customer.setConnectionID(connectionID); //we keep the old connectionID that was in the protocol.
                    customer.setLoggedIn(true);
                    return new AckMessage((short) 2, null);
                }
            }
        }
        return new ErrorMessage((short) 2);
    }
}
