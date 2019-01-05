package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import java.util.concurrent.ConcurrentHashMap;

public class LogOutMessage extends Message {

    public LogOutMessage(byte[] bytes) {
        super((short)3);
    }//there is nothing special to build


    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        Customer customer = protocol.getCustomer();
        if (!customer.isLoggedIn()) //if customer is not registered or not logged in
            return new ErrorMessage((short) 3);
        else return new AckMessage((short) 3, null); //customer is registered and logged in.
    }
}
