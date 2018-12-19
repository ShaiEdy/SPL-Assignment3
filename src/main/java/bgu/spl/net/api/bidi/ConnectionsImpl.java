package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.BlockingConnectionHandler;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl <T> implements Connections<T> {
    ConcurrentHashMap<Integer,BlockingConnectionHandler> connectionHandlerConcurrentHashMap;

    public ConnectionsImpl() {
        this.connectionHandlerConcurrentHashMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, Object msg) {
        BlockingConnectionHandler client= connectionHandlerConcurrentHashMap.get(connectionId);
        client.send(msg); //todo- implement send of BlockingConnectionHandler

        return false; //todo- think how to know if the send succeed or not
    }

    @Override
    public void broadcast(Object msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
