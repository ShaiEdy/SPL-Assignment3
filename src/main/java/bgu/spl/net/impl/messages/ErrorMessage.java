package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.concurrent.ConcurrentHashMap;

public class ErrorMessage extends Message {
    short messageOpcode = 11;
    short otherMessageOpcode;

    public ErrorMessage(short otherMessageOpcode) {
        this.otherMessageOpcode = otherMessageOpcode;
    }

    public short getMessageOpcode() {
        return messageOpcode;
    }

    public short getOtherMessageOpcode() {
        return otherMessageOpcode;
    }

    @Override
    public Message act(DataBase dataBase, Customer customer) {
        return null;
    }
}
