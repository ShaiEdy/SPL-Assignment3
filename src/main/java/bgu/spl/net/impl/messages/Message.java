package bgu.spl.net.impl.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import static bgu.spl.net.api.bidi.MessageEncoderDecoderImp.getBytes;

public abstract class Message { //todo: check if we need to implement something? closeable?
    short opcode;

    public Message(short opcode) {
        this.opcode= opcode;
    }

    public abstract Message act(BidiMessagingProtocolImpl protocol);

    public short getOpcode() {
        return opcode;
    }

    protected byte[] shortToBytes(short num){
        return getBytes(num);
    }

    protected short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    protected byte[] merge2Arrays(byte[] arr1, byte[] arr2) {
        return getBytes(arr1, arr2);
    }

}
