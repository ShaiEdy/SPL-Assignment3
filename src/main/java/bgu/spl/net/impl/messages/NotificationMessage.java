package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import java.util.concurrent.ConcurrentHashMap;

public class NotificationMessage extends Message {
    private short opcode= 9;
    private byte NotificationType; // indicates whether the message is a PM message (0) or a public message (post) (1).
    private String postingUser;
    private String content;

    public NotificationMessage(byte[] messageBytesArray) { //todo maybe delete
        super((short)9);
        NotificationType= messageBytesArray[2];
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

    public NotificationMessage(byte NotificationType, String postingUser, String content) {
        super((short)9);
        this.NotificationType = NotificationType;
        this.postingUser = postingUser;
        this.content = content;
    }

    public byte getNotificationType(){
        return NotificationType;
    }


    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }

    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        return null;
    }
}
