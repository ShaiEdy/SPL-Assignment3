package bgu.spl.net.impl.messages;

public class RegisterMessage<T> extends Message<T> {
    private int arrayLength;
    private int index = 2; // represents the index we are currently looking at. starts from 2 because we dont care about the Opcode.
    private String userName = "";
    private String password = "";

    public RegisterMessage(byte[] messageBytesArray) {
        this.arrayLength = messageBytesArray.length;

        while (index<arrayLength && messageBytesArray[index]!= '\0'){

        }

        for (int i = 2; i < arrayLength; i++) {
        }
    }

    @Override
    public T act(T message) {
        return null;
    }
}
