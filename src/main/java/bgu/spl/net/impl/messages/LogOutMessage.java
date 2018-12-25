package bgu.spl.net.impl.messages;

public class LogOutMessage<T> extends Message<T> {
    public LogOutMessage(byte[] bytes) {}//there is nothing special to build

    @Override
    public T act(T message) {
        return null;
    }
}
