package bgu.spl.net.impl.messages;

public interface Message <T> {
    T act(T message);
}
