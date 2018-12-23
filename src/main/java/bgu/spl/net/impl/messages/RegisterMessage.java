package bgu.spl.net.impl.messages;

public class RegisterMessage<T> implements Message<T> {
    int arrayLength;

    public RegisterMessage(byte[] messageBytesArray) {
        this.arrayLength = messageBytesArray.length;

        for (int i = 2; i < arrayLength; i++) {

        }
    }

    @Override
    public T act(T message) {
        return null;
    }
}
