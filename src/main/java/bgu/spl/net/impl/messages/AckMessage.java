package bgu.spl.net.impl.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

public class AckMessage extends Message {
    private short otherMessageOpcode;
    private byte[] optionalBytesArray;


    public AckMessage(short otherMessageOpcode, byte[] optionalBytesArray) {
        super((short) 10);
        this.otherMessageOpcode = otherMessageOpcode;
        if (optionalBytesArray != null) this.optionalBytesArray = optionalBytesArray;
        else this.optionalBytesArray = new byte[0];
    }

    public short getOtherMessageOpcode() {
        return otherMessageOpcode;
    }

    public byte[] getOptionalBytesArray() {
        return optionalBytesArray;
    }


    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        return null;
    }
}
