package bgu.spl.net.impl.messages;

public class NotificationMessage<T> extends Message<T> {
    private char notificationByte;
    private String postingUser;
    private String content;

    public NotificationMessage(byte[] messageBytesArray) {
        notificationByte= (char)messageBytesArray[2];
        int index= 3;
        while (messageBytesArray[index]!='\0'){  //complete the user name string
            postingUser+=Byte.toString(messageBytesArray[index]);
            index++;
        }
        index++; // we passed the posting user name and we want to pass the 0 and get to content
        while (messageBytesArray[index]!='\0'){  //complete the user name string
            content+=Byte.toString(messageBytesArray[index]);
            index++;
        }
    }
    @Override
    public T act(T message) {
        return null;
    }
}
