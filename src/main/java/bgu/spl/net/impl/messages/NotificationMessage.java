package bgu.spl.net.impl.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

public class NotificationMessage extends Message {
    private byte NotificationType; // indicates whether the message is a PM message (0) or a public message (post) (1).
    private String postingUser;
    private String content;

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
