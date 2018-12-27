package bgu.spl.net.impl.messages;

public class AckMessage<T> extends Message<T>{
    short messageOpcode;
    byte[] optionalBytesArray;

    public AckMessage(byte [] messageByteArray) {
        byte[] byteArrMsgOpcose= {messageByteArray[2], messageByteArray[3]};
        messageOpcode= bytesToShort(byteArrMsgOpcose);
        int index= 4;
        int optionalIndex=0;
        while (messageByteArray[index]<messageByteArray.length){  //copy the optional bytes if exist
            optionalBytesArray[optionalIndex]= messageByteArray[index];
            index++; optionalIndex++;
        }
    }

    @Override
    public T act(T message) {
        return null;
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }
}


