package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.concurrent.ConcurrentHashMap;

public class AckMessage extends Message {
    private short otherMessageOpcode;
    private byte[] optionalBytesArray;

    public AckMessage(byte[] messageByteArray) { //todo- decide if needed
        byte[] byteArrMsgOpcose = {messageByteArray[2], messageByteArray[3]};
        otherMessageOpcode = bytesToShort(byteArrMsgOpcose);
        int index = 4;
        int optionalIndex = 0;
        while (messageByteArray[index] < messageByteArray.length) {  //copy the optional bytes if exist
            optionalBytesArray[optionalIndex] = messageByteArray[index];
            index++;
            optionalIndex++;
        }
    }

    public AckMessage(short otherMessageOpcode, byte[] optionalBytesArray) {
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
    public Message act(DataBase dataBase, Customer customer) {
        return null;
    }
}