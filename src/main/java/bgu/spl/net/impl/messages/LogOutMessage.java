package bgu.spl.net.impl.messages;

public class LogOutMessage<T> extends Message<T> {
    public LogOutMessage(byte[] bytes) {
    }

    @Override
    public T act(T message) {
        return null;
    }
}
