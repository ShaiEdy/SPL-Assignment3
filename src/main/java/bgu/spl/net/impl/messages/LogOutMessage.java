package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import java.util.concurrent.ConcurrentHashMap;

public class LogOutMessage extends Message {
    public LogOutMessage(byte[] bytes) {}//there is nothing special to build

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap) {
        return null;
    }
}
