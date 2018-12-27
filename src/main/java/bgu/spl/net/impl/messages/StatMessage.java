package bgu.spl.net.impl.messages;

public class StatMessage <T> extends Message<T> {
    private String userName;

    public StatMessage(byte[] messageBytesArray) {
        int index= 2;
        while (messageBytesArray[index]!='\0'){  //complete the user name string
            userName+=Byte.toString(messageBytesArray[index]);
            index++;
        }
    }

    @Override
    public T act(T message) {
        return null;
    }
}
