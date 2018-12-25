package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.messages.LogOutMessage;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.impl.messages.PostMessage;

import java.util.Arrays;
import java.util.Vector;

public class MessageEncoderDecoderImp<T> implements MessageEncoderDecoder<T> { //T is message
    private byte [] bytes;
    private int len;
    private short opcode;
    private boolean isMessage;
    private T newMessage;

    public MessageEncoderDecoderImp() {
        byte [] bytes;
        len=0;
        isMessage= false;
    }
    /*private Message convertStringToMessage (byte[] messageArray) {//todo maybe remove to encdec
        /*short opcode = bytesToShort(messageArray);
        if (opcode==0) return new RegisterMessage(messageArray);
        else if (opcode==1) return new LogInMessage(messageArray);
        else if (opcode==2) return new LogOutMessage(messageArray);
        else if (opcode==3) return new FollowMessage(messageArray);
        else if (opcode==4) return new PostMessage(messageArray);
        else if (opcode==5) return new PMMessage(messageArray);
        else if (opcode==6) return new UserListMessage(messageArray);
        else if(opcode==7) return new StatMessage(messageArray);

        return null; //todo- add else before the return null
    }
    */

    @Override
    public T decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        if (len==1) {
            return null; //not a line yet
        }
        if (len==2) {
            this.opcode= bytesToShort(bytes);
        }

        if (len>=2) {
            if (opcode == 1| opcode==2) {
                //todo write this
            }
            if (opcode==3){ //unregister is only the opcode
                Message message = new LogOutMessage(bytes);
                resolve((T) message);
                return popMessage();
            }
            if (opcode==4){
                //todo its follow and its shit... for tomorrow
            }
            if (opcode==5){
                if (nextByte == '\0') {
                    Message message = new PostMessage(bytes);
                    resolve((T) message);
                    popMessage();
                }
            } //todo to be continue

        }
    return null; //will return null only if the message was not completed
    }

    private void resolve (T message){
        this.newMessage = message;
        isMessage = true;
    }
    private T popMessage(){
        isMessage= true;
        len = 0;
        return newMessage;
        //todo- should I "clean" the  bytes array? (they do not clean it in echo)
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
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }



}
