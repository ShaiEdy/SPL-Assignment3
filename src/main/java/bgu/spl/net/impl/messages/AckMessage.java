package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

import java.util.concurrent.ConcurrentHashMap;

public class AckMessage extends Message{
    short messageOpcode;
    byte[] optionalBytesArray;

    public AckMessage(byte [] messageByteArray) { //todo- decide if needed
        byte[] byteArrMsgOpcose= {messageByteArray[2], messageByteArray[3]};
        messageOpcode= bytesToShort(byteArrMsgOpcose);
        int index= 4;
        int optionalIndex=0;
        while (messageByteArray[index]<messageByteArray.length){  //copy the optional bytes if exist
            optionalBytesArray[optionalIndex]= messageByteArray[index];
            index++; optionalIndex++;
        }
    }

    public AckMessage(short messageOpcode, byte[]optionalBytesArray ) {
        this.messageOpcode= messageOpcode;
        this.optionalBytesArray= optionalBytesArray;
    }


    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer) {
        return null;
    }
}


