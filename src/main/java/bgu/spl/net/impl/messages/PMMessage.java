package bgu.spl.net.impl.messages;

public class PMMessage<T> extends Message<T> {
    String userName;
    String content;

    public PMMessage(byte [] bytes) {
        int index=2;
        while (bytes[index]!='\0'){
            userName += Byte.toString((bytes[index]));  // we append the userName with the next byte.
            index++;
        }
        index++; //pass the \0 byte
        while (bytes[index]!='\0'){
            content += Byte.toString((bytes[index]));  // we append the content with the next byte.
            index++;
        }
    }

    @Override
    public T act(T message) {
        return null;
    }
}
