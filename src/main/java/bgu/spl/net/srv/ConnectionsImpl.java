package bgu.spl.net.srv;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {
    private ConcurrentHashMap<Integer, ConnectionHandler> idToConnectionHandler; //all the clients that connected to the server
    private ConcurrentHashMap<Integer, Customer> idToCustomer;

    public ConnectionsImpl() {
        this.idToConnectionHandler = new ConcurrentHashMap<>();
        this.idToCustomer = new ConcurrentHashMap<>(); //  todo  delete the comment- need to be added here when someone do register
    }

    public void addNewConnection(int connectionId, ConnectionHandler CH) {
        idToConnectionHandler.put(connectionId, CH);
    }


    @Override
    @SuppressWarnings("unchecked")
    //send the msg to the CH with the given id
    public boolean send(int connectionId, T msg) {
        ConnectionHandler CH = idToConnectionHandler.get(connectionId);
        CH.send(msg);
        return false; //todo- think how to know if the send succeed or not
    }

    @Override
    @SuppressWarnings("unchecked")
    //send the broadcast to whoever registered
    public void broadcast(T msg) {
        Set<Integer> idSet = idToConnectionHandler.keySet();
        for (Integer integer : idSet) {
            ConnectionHandler CH = idToConnectionHandler.get(integer);
            CH.send(msg);
        }
    }

    @Override
    //remove the CH with the given id from the hashMap
    public void disconnect(int connectionId) {
        idToConnectionHandler.remove(connectionId);
    }
}
