package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

import java.util.concurrent.ConcurrentHashMap;

public class StatMessage extends Message {
    private String userName;

    public StatMessage(byte[] messageBytesArray) {
        int index= 2;
        while (messageBytesArray[index]!='\0'){  //complete the user name string
            userName+=Byte.toString(messageBytesArray[index]);
            index++;
        }
    }

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer) {
        return null;
    }
}
