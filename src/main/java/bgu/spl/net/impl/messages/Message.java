package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

public abstract class Message { //todo: check if we need to implement something? closeable?


    protected abstract Message act(DataBase dataBase, Customer customer);

    protected byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
