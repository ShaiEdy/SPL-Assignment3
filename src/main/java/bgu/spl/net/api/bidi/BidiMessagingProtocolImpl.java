package bgu.spl.net.api.bidi;

import bgu.spl.net.impl.messages.LogInMessage;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.impl.messages.RegisterMessage;

import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    private ConcurrentHashMap<Integer,Connections<T>> idToConnections;
    private boolean shouldTerminate;

    public BidiMessagingProtocolImpl() {
        this.idToConnections = new ConcurrentHashMap<>();
        this.shouldTerminate = false;
    }

    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    @Override
    public void start(int connectionId, Connections connections) {
        idToConnections.put(connectionId,connections);
    }

    @Override
    public void process(T message) { //todo we want to make a message object which has field of short and field of string

    }

    /**
     * @return true if the connection should be terminated
     */
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    private Message convertStringToMessage (byte[] messageArray) {
        short opcode = bytesToShort(messageArray);
        if (opcode==0) return new RegisterMessage();
        if (opcode==1) return new LogInMessage();
    }



    /**
     * @param byteArr- array of bytes represents the message
     * @return short represent by the two first byte of the array
     */
    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}
