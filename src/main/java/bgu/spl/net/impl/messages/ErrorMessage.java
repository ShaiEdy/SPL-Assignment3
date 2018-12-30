package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

public class ErrorMessage<T> extends Message<T> {
    short messageOpcode;

    public ErrorMessage(byte [] messageBytesArray) {
        byte[] byteArrMsgOpcose= {messageBytesArray[2], messageBytesArray[3]};
        messageOpcode= bytesToShort(byteArrMsgOpcose);
    }

    @Override
    protected T act(T message) {
        return null;
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }
}
