package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

import java.util.concurrent.ConcurrentHashMap;

public class RegisterMessage extends Message {
    private int arrayLength;
    private String userName = "";
    private String password = "";


    public RegisterMessage(byte[] messageBytesArray) {
        this.arrayLength = messageBytesArray.length;

        int index = 2; // represents the index we are currently looking at. starts from 2 because we dont care about the Opcode.

        appendToString(messageBytesArray, userName, index);
        index++;
        appendToString(messageBytesArray, password, index);
    }

    /**
     * Used to make a String of all the bytes from messageBytesArray[index] to the first /0 in messageBytesArray.
     **/
    private void appendToString(byte[] messageBytesArray, String stringToAppendTo, int index) {
        while (messageBytesArray[index] != '\0') {
            stringToAppendTo += Byte.toString(messageBytesArray[index]); // we append the userName with the next byte.
            index++;
        }
    }

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap,Customer customer) {
        //first we will check that this customer is not already registered.
        if (customer!=null) //if customer was already registered.
            return new ErrorMessage((short)1);


        else { // if username is not already registered
            customer = new Customer(userName, password); //create the customer.
            dataMap.put(userName, customer); // put it in the data map
            //todo: do we need to think about problems caused by multiThreading? what if two clients register at the same time with the same user name?

            return new AckMessage((short)1,null);

        }
    }
}
