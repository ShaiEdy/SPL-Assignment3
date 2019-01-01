package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class UserListMessage extends Message {

    public UserListMessage(byte[] bytes) {
        super();
    }



    @Override
    protected Message act(DataBase dataBase, Customer customer) {
        if (!customer.isLoggedIn()){
            return new ErrorMessage((short) 7);
        }
        Set<String> registerNamesSet= dataBase.getUserNameToCustomer().keySet();
        Integer numOfRegisters= registerNamesSet.size(); //num of registersUsers
        String registerListString= numOfRegisters.toString();//String that will be send as optional bytes array
        for (String name: registerNamesSet){
            registerListString+= name+"\0";  //filling the string with the names of the register users
        }
        return new AckMessage((short)7,registerListString.getBytes()); //string converted to array bytes
    }
}
