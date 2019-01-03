package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.concurrent.ConcurrentHashMap;

public class LogOutMessage extends Message {
    private short opcode= 2;

    public LogOutMessage(byte[] bytes) {
        super((short)2);
    }//there is nothing special to build


    @Override
    public Message act(DataBase dataBase, Customer customer) {
        if (!customer.isLoggedIn()) //if customer is not registered or not logged in
            return new ErrorMessage((short) 3);
        else return new AckMessage((short) 3, null); //customer is registered and logged in.
    }
}
