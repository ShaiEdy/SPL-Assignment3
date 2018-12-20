package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.BlockingConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl <T> implements Connections<T> {
    ConcurrentHashMap<Integer,BlockingConnectionHandler> idToConnectionHandler;

    public ConnectionsImpl() {
        this.idToConnectionHandler = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, Object msg) {
        BlockingConnectionHandler CH= idToConnectionHandler.get(connectionId);
        CH.send(msg); //todo- implement send of BlockingConnectionHandler

        return false; //todo- think how to know if the send succeed or not
    }

    @Override
    public void broadcast(Object msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
