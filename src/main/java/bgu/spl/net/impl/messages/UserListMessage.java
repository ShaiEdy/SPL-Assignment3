package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

import java.util.concurrent.ConcurrentHashMap;

public class UserListMessage extends Message {

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer) {
        return null;
    }
}
