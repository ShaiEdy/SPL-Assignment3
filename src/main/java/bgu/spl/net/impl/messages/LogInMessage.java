package bgu.spl.net.impl.messages;

public class LogInMessage<T> extends Message<T> {
    private int arrayLength;
    private int index = 2; // represents the index we are currently looking at. starts from 2 because we dont care about the Opcode.
    private String userName = "";
    private String password = "";

    public LogInMessage(byte[] messageBytesArray) {
        this.arrayLength = messageBytesArray.length;

        appendToString(messageBytesArray,userName);
        index++;
        appendToString(messageBytesArray,password);

    }


    /**
     * Used to make a String of all the bytes from messageBytesArray[index] to the first /0 in messageBytesArray.
     **/
    private void appendToString(byte[] messageBytesArray, String stringToAppendTo){
        while (messageBytesArray[index] != '\0') {
            stringToAppendTo += Byte.toString(messageBytesArray[index]); // we append the userName with the next byte.
            index++;

        }
    }

    @Override
    public T act(T message) {
        return null;
    }
}
