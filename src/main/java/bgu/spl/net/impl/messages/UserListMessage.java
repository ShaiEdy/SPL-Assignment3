package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import java.util.Set;

public class UserListMessage extends Message {

    public UserListMessage(byte[] bytes) {
    } //nothing special to build

    @Override
    public Message act(DataBase dataBase, Customer customer) {
        if (!customer.isLoggedIn()) {
            return new ErrorMessage((short) 7);
        }
        Set<String> registerNamesSet = dataBase.getUserNameToCustomer().keySet();
        byte[] numOfRegistersBytesArr = shortToBytes((short) registerNamesSet.size());//num of registersUsers
        String registerListString = "";//String that will be send as optional bytes array
        for (String name : registerNamesSet) {
            registerListString += name + "\0";  //filling the string with the names of the register users
        }
        byte[] ackMessageOptionalArr = merge2Arrays(numOfRegistersBytesArr, registerListString.getBytes());

        return new AckMessage((short) 7, ackMessageOptionalArr); //string converted to array bytes
    }
}
