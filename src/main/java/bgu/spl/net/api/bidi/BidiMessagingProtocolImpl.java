package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    private int connectionId;
    private Connections connections;
    private boolean shouldTerminate;
    private DataBase dataBase;
    private Customer customer; //this object will never be initialized in the protocol. only in RegisterMessage.


    public BidiMessagingProtocolImpl(DataBase dataBase) {
        this.dataBase = dataBase;
        this.shouldTerminate = false;
        this.customer = new Customer();
    }
    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        this.customer.setConnectionID(connectionId);
    }

    @Override
    public void process(T message) { // get a specific message that was created in encoder decoder
        //use here the act messages of the messages for doing the specific process that needed
        //we will send back a response using connections.

    }

    /**
     * @return true if the connection should be terminated
     */
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }


    /**
     * @param byteArr- array of bytes represents the message
     * @return short represent by the two first byte of the array
     */
    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }
}
