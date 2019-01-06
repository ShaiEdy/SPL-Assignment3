package bgu.spl.net.impl.messages;

import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class UserListMessage extends Message {
    private short opcode= 7;

    public UserListMessage(byte[] bytes) {
        super((short)7);

    } //nothing special to build

    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        DataBase dataBase = protocol.getDataBase();

        if (!protocol.isLoggedIn()) {
            return new ErrorMessage((short) 7);
        }

        Set<String> registerNamesSet = dataBase.getUserNameToCustomer().keySet();
        byte[] numOfRegistersBytesArr = shortToBytes((short) registerNamesSet.size());//num of registersUsers
        List<Byte> bytesToReturn = new Vector<>(convertbytesArrayToBytesList(numOfRegistersBytesArr));

        for (String name : registerNamesSet) {
            bytesToReturn.addAll(convertbytesArrayToBytesList(name.getBytes()));// add the notification type
            bytesToReturn.add((byte) '\0');
        }

        byte[] ArrayToReturn = convertListToArray(bytesToReturn); //convert to byte[]

        return new AckMessage((short) 7, ArrayToReturn); //string converted to array bytes
    }
    private byte[] convertListToArray(List<Byte> bytes){
        byte[] bytestoreturn= new byte[bytes.size()];
        int index=0;
        for (Byte Byte: bytes){
            bytestoreturn[index]= Byte;
            index++;
        }
        return bytestoreturn;
    }

    private List<Byte> convertbytesArrayToBytesList(byte[] bytes){
        Byte[] Bytes = new Byte[bytes.length];
        int index=0;
        for(byte b: bytes) {
            Bytes[index] = b;
            index++;
        }
        return Arrays.asList(Bytes);
    }
}
