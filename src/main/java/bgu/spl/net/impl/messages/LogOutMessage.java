package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import java.util.concurrent.ConcurrentHashMap;

public class LogOutMessage extends Message {
    public LogOutMessage(byte[] bytes) {}//there is nothing special to build


    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer) {
        if (customer == null || !customer.isLogin()) //if customer is not registered or not logged in
            return new ErrorMessage((short) 3);
        else return new AckMessage((short) 3, null); //customer is registered and logged in.

    }
}
