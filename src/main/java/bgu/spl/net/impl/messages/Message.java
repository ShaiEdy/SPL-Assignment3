package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

public abstract class Message<T> { //todo: check if we need to implement something? closeable?

    protected abstract T act(T message);

   // protected abstract T act(T message, Customer customer);
}
