package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.messages.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class MessageEncoderDecoderImp implements MessageEncoderDecoder<Message> { //T is message
    private byte[] bytes = new byte[1 << 10]; //start with 1k //todo think
    private int len = 0;
    private short opcode = 0;
    private int countNumOfZeros = 0; // we will use this counter to count \0 bytes
    private int numOfUserCounter = 0; //we will use this counter to count users.

    public MessageEncoderDecoderImp() {}

    @Override
    public Message decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        if (len == 2)
            opcode = bytesToShort(bytes);

        if (opcode == 1 | opcode == 2) { // register or login.
            Message toReturn = handleRegisterOrLoginMessage();
            if (toReturn != null) return toReturn;
        }
        if (opcode == 3) { // logout.
            makeVariablesZero();
            return new LogOutMessage(bytes);
        }
        if (opcode == 4) { // follow.
            Message toReturn = handleFollowMessage();
            if (toReturn != null) return toReturn;
        }
        if (opcode == 5) { // Post
            if (bytes[len - 1] == '\0') {
                makeVariablesZero();
                return new PostMessage(bytes);
            }
        }
        if (opcode == 6) { // PM
            Message toReturn = handlePM();
            if (toReturn != null) return toReturn;
        }
        if (opcode == 7) { // UserList
            makeVariablesZero();
            return new UserListMessage(bytes);
        }
        if (opcode == 8) { //Stat
            if (bytes[len - 1] == '\0') {
                makeVariablesZero();
                return new StatMessage(bytes);
            }
        }
        return null; //not a line yet
    }

    private Message handlePM() {
        if (bytes[len-1] == '\0') countNumOfZeros++;
        if (countNumOfZeros == 2) {
            makeVariablesZero();
            return new PMMessage(bytes);
        }
        return null;
    }

    private Message handleFollowMessage() {
        if (len == 5) {
            byte[] numOfUsersArr = new byte[2];
            numOfUsersArr[0] = bytes[3];
            numOfUsersArr[1] = bytes[4];
            numOfUserCounter = bytesToShort(numOfUsersArr);
        }
        if (bytes[len-1] == '\0') {
            countNumOfZeros++;
            if (countNumOfZeros == numOfUserCounter + 1) {
                makeVariablesZero();
                return new FollowMessage(bytes);
            }
        }
        return null;
    }

    private void makeVariablesZero(){
        opcode = 0;
        len = 0;
        countNumOfZeros = 0;
        numOfUserCounter = 0;
    }

    private Message handleRegisterOrLoginMessage(){
        if (bytes[len-1]== '\0') countNumOfZeros ++;
        if (countNumOfZeros == 2) {
            int opcodeTmp = opcode;
            makeVariablesZero();
            if (opcodeTmp == 1) return new RegisterMessage(bytes);
            else return new LogInMessage(bytes);
        }
        return null;
    }

    private void pushByte(byte nextByte) { //push the new byte to the array (and extend it if needed)
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len] = nextByte;
        len++;
    }

    @Override
    public byte[] encode(Message message) {
        //this method gets one of the following: Ack/Error/Notification Message and changes it to array of bytes.
        List<Byte> bytesToReturn= new Vector<>();
        byte[] ArrayToReturn;

        int opcode = message.getOpcode();
        //---------------ErrorMessage------------//
        if (opcode == 11) {
            byte[] errorMessageOpcodeBytesArr = shortToBytes((short) 11);
            byte[] otherMessageOpcodeBytesArr = shortToBytes(((ErrorMessage) message).getOtherMessageOpcode());
            ArrayToReturn = merge2Arrays(errorMessageOpcodeBytesArr,otherMessageOpcodeBytesArr);
        }
        //---------------AckMessage------------//
        else if (opcode == 10) {
            byte[] notificationMessageOpcodeBytesArr = shortToBytes((short) 10);
            byte[] otherMessageOpcodeBytesArr = shortToBytes(((AckMessage) message).getOtherMessageOpcode());
            ArrayToReturn = merge2Arrays(notificationMessageOpcodeBytesArr,otherMessageOpcodeBytesArr);
            byte[] optionalBytesArr = ((AckMessage) message).getOptionalBytesArray();
            ArrayToReturn = merge2Arrays(ArrayToReturn,optionalBytesArr);
        }
        //---------------NotificationMessage-----//
        else{ //if message is NotificationMessage
            bytesToReturn.addAll(convertbytesArrayToBytesList(shortToBytes((short) 9)));// add the opcode bytes
            bytesToReturn.add(((NotificationMessage) message).getNotificationType());// add the notification type
            String postingUser= ((NotificationMessage) message).getPostingUser();
            bytesToReturn.addAll(convertbytesArrayToBytesList(postingUser.getBytes()));//add posting user name
            bytesToReturn.add((byte) '\0');
            String content= ((NotificationMessage) message).getContent();
            bytesToReturn.addAll(convertbytesArrayToBytesList(content.getBytes())); // add the content
            bytesToReturn.add((byte) '\0');
            ArrayToReturn= convertListToArray(bytesToReturn); //convert to byte[]
        }

        return ArrayToReturn;
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

    private short bytesToShort(byte[] byteArr) {

        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    private byte[] shortToBytes(short num) {
        return getBytes(num);
    }

    public static byte[] getBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    private byte[] merge2Arrays(byte[] arr1, byte[] arr2) {
        return getBytes(arr1, arr2);
    }

    public static byte[] getBytes(byte[] arr1, byte[] arr2) {
        byte[] toReturn = new byte[arr1.length + arr2.length];
        int index = 0;
        for (byte b : arr1) {
            toReturn[index] = b;
            index++;
        }
        for (byte b : arr2) {
            toReturn[index] = b;
            index++;
        }
        return toReturn;
    }

}