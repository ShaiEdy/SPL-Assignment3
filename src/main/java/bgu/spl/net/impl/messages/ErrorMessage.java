package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.concurrent.ConcurrentHashMap;

public class ErrorMessage extends Message {
    private short opcode=11;
    private short otherMessageOpcode;

    public ErrorMessage(short otherMessageOpcode) {
        super((short) 11);
        this.otherMessageOpcode = otherMessageOpcode;
    }

    public short getOtherMessageOpcode() {
        return otherMessageOpcode;
    }

    @Override
    public Message act(DataBase dataBase, Customer customer) {
        return null;
    }
}
