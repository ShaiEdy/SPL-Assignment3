package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.messages.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class MessageEncoderDecoderImp<Message> implements MessageEncoderDecoder<Message> { //T is message
    private byte[] bytes = new byte[1 << 10]; //start with 1k //todo think
    private int len = 0;

    public MessageEncoderDecoderImp() {
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (nextByte == '\n') {
            pushByte(nextByte);
            return popMessage();
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    @SuppressWarnings("unchecked")
    private Message popMessage() { // we enter popMessage only when the message is done.
        len = 0;
        //todo- should I "clean" the  bytes array? (they do not clean it in echo)
        short opcode = bytesToShort(bytes); //first we need to get the opcode from the first two byte.
        if (opcode == 1){ return (Message)new RegisterMessage(bytes); }
        else if (opcode == 2) return (Message) new LogInMessage(bytes);
        else if (opcode == 3) return (Message) new LogOutMessage(bytes);
        else if (opcode == 4) return (Message) new FollowMessage(bytes);
        else if (opcode == 5) return (Message) new PostMessage(bytes);
        else if (opcode == 6) return (Message) new PMMessage(bytes);
        else if (opcode == 7) return (Message) new UserListMessage(bytes);
        else if (opcode == 8) return (Message) new StatMessage(bytes);
        return null;
    }

    private void pushByte(byte nextByte) { //push the new byte to the array (and extend it if needed)
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    @Override
    public byte[] encode(Message message) {
        //this method gets one of the following: Ack/Error/Notification Message and changes it to array of bytes.
        List<Byte> bytesToReturn= new Vector<>();
        byte[] ArrayToReturn;

        //---------------ErrorMessage------------//
        if (message instanceof ErrorMessage) {
            byte[] errorMessageOpcodeBytesArr = shortToBytes((short) 11);
            byte[] otherMessageOpcodeBytesArr = shortToBytes(((ErrorMessage) message).getOtherMessageOpcode());
            ArrayToReturn = merge2Arrays(errorMessageOpcodeBytesArr,otherMessageOpcodeBytesArr);
        }
        //---------------AckMessage------------//
        else if (message instanceof AckMessage) {
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
            Bytes[index++] = b;
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