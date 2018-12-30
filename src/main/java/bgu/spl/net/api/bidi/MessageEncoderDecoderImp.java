package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.messages.*;
import java.util.Arrays;

public class MessageEncoderDecoderImp<T> implements MessageEncoderDecoder<T> { //T is message
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    public MessageEncoderDecoderImp() {}

    @Override
    public T decodeNextByte(byte nextByte) {
        if (nextByte == '\n') {
            pushByte(nextByte);
            return popMessage();
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    private T popMessage() { // we enter popMessage only when the message is done.
        len = 0;
        //todo- should I "clean" the  bytes array? (they do not clean it in echo)

        short opcode = bytesToShort(bytes); //first we need to get the opcode from the first two byte.

        if (opcode == 1) return (T) new RegisterMessage(bytes);
        else if (opcode == 2) return (T) new LogInMessage(bytes);
        else if (opcode == 3) return (T) new LogOutMessage(bytes);
        else if (opcode == 4) return (T) new FollowMessage(bytes);
        else if (opcode == 5) return (T) new PostMessage(bytes);
        else if (opcode == 6) return (T) new PMMessage(bytes);
        else if (opcode == 7) return (T) new UserListMessage(bytes);
        else if (opcode == 8) return (T) new StatMessage(bytes);

        return null;
    }

    private void pushByte(byte nextByte) { //push the new byte to the array (and extend it if needed)
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    @Override
    public byte[] encode(T message) {
        return new byte[0];
        //todo: implement
    }

    private short bytesToShort(byte[] byteArr) {

        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

}