package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.concurrent.ConcurrentHashMap;

public class LogOutMessage extends Message {
    public LogOutMessage(byte[] bytes) {}//there is nothing special to build


    @Override
    public Message act(DataBase dataBase, Customer customer) {
        if (customer == null || !customer.isLoggedIn()) //if customer is not registered or not logged in
            return new ErrorMessage((short) 3);
        else return new AckMessage((short) 3, null); //customer is registered and logged in.
    }
}
