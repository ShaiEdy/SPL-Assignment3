package bgu.spl.net.impl.messages;
import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

public class LogOutMessage extends Message {

    public LogOutMessage(byte[] bytes) {
        super((short) 3);
    }//there is nothing special to build


    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        DataBase dataBase = protocol.getDataBase();

        if (protocol.isLoggedIn()) {//if customer is not registered or not logged in
            String thisCustomerUserName = protocol.getUserName();
            Customer thisCustomer = dataBase.getUserNameToCustomer().get(thisCustomerUserName);

            if (thisCustomer.isLoggedIn()) {
                synchronized (thisCustomer) {// sync for preventing post/pm to be sent while customer is log out
                    thisCustomer.setLoggedIn(false);
                }
                //dealing with terminate the connection
                protocol.setShouldTerminate(true); //only if logout succeed (we send the client log out ack) so we terminate
                return new AckMessage((short) 3, null); //customer is registered and logged in.
            }
        }
        return new ErrorMessage((short) 3);
    }
}
