package bgu.spl.net.impl.messages;

public class PostMessage<T> extends Message<T> {
    public PostMessage(T bytes) {
    }

    @Override
    public T act(T message) {
        return null;
    }
}
