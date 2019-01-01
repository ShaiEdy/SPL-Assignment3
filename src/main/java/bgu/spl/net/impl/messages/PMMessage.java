package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.concurrent.ConcurrentHashMap;

public class PMMessage extends Message {
    String userName;
    String content;

    public PMMessage(byte [] bytes) {
        int index=2;
        while (bytes[index]!='\0'){
            userName += Byte.toString((bytes[index]));  // we append the userName with the next byte.
            index++;
        }
        index++; //pass the \0 byte
        while (bytes[index]!='\0'){
            content += Byte.toString((bytes[index]));  // we append the content with the next byte.
            index++;
        }
    }

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer) {
       dataMap.get(userName).addPM(content);
       customer.addPM(content);
       NotificationMessage notificationMessage= new NotificationMessage();

        return null;
    }

    @Override
    protected Message act(DataBase dataBase, Customer customer) {
        dataBase.getUserNameToCustomer().get(userName).addPM(content);
        customer.addPM(content);
        NotificationMessage notificationMessage= new NotificationMessage((char) 0,userName, content);
        return null;
    }
}
