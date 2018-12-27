package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

public class ErrorMessage<T> extends Message<T> {
    @Override
    protected T act(T message) {
        return null;
    }

}
