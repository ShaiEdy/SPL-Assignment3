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
        //Customer customer = protocol.getCustomer();
        DataBase dataBase = protocol.getDataBase();

        if (protocol.isLoggedIn()) {//if customer is not registered or not logged in
            String thisCustomerUserName = protocol.getUserName();
            Customer thisCustomer = dataBase.getUserNameToCustomer().get(thisCustomerUserName);
            if (thisCustomer.isLoggedIn()) {
                thisCustomer.setLoggedIn(false);
                return new AckMessage((short) 3, null); //customer is registered and logged in.
            }
        }
        return new ErrorMessage((short) 3);

    }
}
