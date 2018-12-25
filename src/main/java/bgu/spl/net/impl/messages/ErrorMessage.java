package bgu.spl.net.impl.messages;

public class ErrorMessage<T> extends Message<T> {
    @Override
    protected T act(T message) {
        return null;
    }
}
