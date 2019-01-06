package bgu.spl.net.impl.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

public class ErrorMessage extends Message {
    private short otherMessageOpcode;

    public ErrorMessage(short otherMessageOpcode) {
        super((short) 11);
        this.otherMessageOpcode = otherMessageOpcode;
    }

    public short getOtherMessageOpcode() {
        return otherMessageOpcode;
    }

    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        return null;
    }
}
