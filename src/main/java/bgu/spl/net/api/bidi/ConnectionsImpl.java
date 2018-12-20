package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.BlockingConnectionHandler;

import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl <T> implements Connections<T> {
    ConcurrentHashMap<Integer,BlockingConnectionHandler> idToConnectionHandler;

    public ConnectionsImpl() {
        this.idToConnectionHandler = new ConcurrentHashMap<>();
    }

    @Override
    //send the msg to the CH with the given id
    public boolean send(int connectionId, T msg) {
        BlockingConnectionHandler blockingCH= idToConnectionHandler.get(connectionId);
        blockingCH.send(msg);
        return false; //todo- think how to know if the send succeed or not
    }

    @Override
    //send the broadcast to whoever registered
    public void broadcast(T msg) {
        Set<Integer> idSet=idToConnectionHandler.keySet();
        for (Integer integer: idSet){
            BlockingConnectionHandler blockingCH= idToConnectionHandler.get(integer);
            blockingCH.send(msg);
        }
    }

    @Override
    //remove the CH with the given id from the hashMap
    public void disconnect(int connectionId) {
        idToConnectionHandler.remove(connectionId);
    }
}
