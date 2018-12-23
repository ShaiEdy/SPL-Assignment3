package bgu.spl.net.api.bidi;

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
}
