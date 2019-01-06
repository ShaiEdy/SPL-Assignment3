package bgu.spl.net.api.bidi;

import bgu.spl.net.api.DataBase;
import bgu.spl.net.impl.messages.*;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {
    private int connectionId;
    private Connections connections;
    private boolean shouldTerminate;
    private DataBase dataBase;
    private boolean isLoggedIn = false;
    private String userName = "";


    public BidiMessagingProtocolImpl(DataBase dataBase) {
        this.dataBase = dataBase;
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
    @SuppressWarnings("unchecked")
    public void process(Message message) {
        // get a specific message that was created in encoder decoder
        //we use here the act messages of the messages for doing the specific process that needed
        //we will send back a response using connections.
        Message newMessage= message.act(this);
        connections.send(connectionId,newMessage); // send ack/ error to the client
    }

    /**
     * @return true if the connection should be terminated
     */
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void setShouldTerminate(boolean shouldTerminate) {
        this.shouldTerminate = shouldTerminate;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public Connections getConnections() {
        return connections;
    }
}
