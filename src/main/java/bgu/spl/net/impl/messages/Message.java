package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

public abstract class Message { //todo: check if we need to implement something? closeable?

    protected abstract Message act(DataBase dataBase, Customer customer);
}
