package bgu.spl.net.impl.messages;

public abstract class Message<T> { //todo: check if we need to implement something? closeable?

    protected abstract T act(T message);
}
