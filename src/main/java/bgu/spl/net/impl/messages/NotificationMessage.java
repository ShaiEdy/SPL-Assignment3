package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

import java.util.concurrent.ConcurrentHashMap;

public class NotificationMessage extends Message {
    private char notificationByte;
    private String postingUser;
    private String content;

    public NotificationMessage(byte[] messageBytesArray) {
        notificationByte= (char)messageBytesArray[2];
        int index= 3;
        while (messageBytesArray[index]!='\0'){  //complete the user name string
            postingUser+=Byte.toString(messageBytesArray[index]);
            index++;
        }
        index++; // we passed the posting user name and we want to pass the 0 and get to content
        while (messageBytesArray[index]!='\0'){  //complete the user name string
            content+=Byte.toString(messageBytesArray[index]);
            index++;
        }
    }

    public NotificationMessage() {

    }

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer) {
        return null;
    }
}
