package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

public abstract class Message { //todo: check if we need to implement something? closeable?


    public abstract Message act(DataBase dataBase, Customer customer);

    protected byte[] shortToBytes(short num){
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    protected byte[] merge2Arrays(byte[] arr1, byte[] arr2) {
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
