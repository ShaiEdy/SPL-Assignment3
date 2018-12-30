package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Customer;

import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    private int connectionId;
    private Connections connections;
    private boolean shouldTerminate;
    private ConcurrentHashMap<String,Customer> dataMap;
    private Customer customer = null; //this object will never be initialized in the protocol. only in RegisterMessage.


    public BidiMessagingProtocolImpl(ConcurrentHashMap dataMap) {
        this.dataMap = dataMap;
        this.shouldTerminate = false;
    }

    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
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
