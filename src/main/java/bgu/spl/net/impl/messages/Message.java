package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

import java.util.concurrent.ConcurrentHashMap;

public abstract class Message { //todo: check if we need to implement something? closeable?

    protected abstract Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer);
}
