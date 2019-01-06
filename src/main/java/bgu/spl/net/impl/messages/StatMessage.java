package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;


public class StatMessage extends Message {
    private String userName; // the user name of the user we want to get his status

    public StatMessage(byte[] messageBytesArray) {
        super((short)8);

        int index= 2;
        int counterNameBytes=0;
        int primeIndex=index;
        while (messageBytesArray[index]!='\0'){  //counting the user name bytes
            counterNameBytes++;
            index++;
        }
        byte [] userNameBytes =new  byte [counterNameBytes];
        for (int i = 0; i <counterNameBytes ; i++) { // creating bytes array of the user name
            userNameBytes[i]= messageBytesArray[primeIndex];
            primeIndex++;
        }
        userName= new String(userNameBytes);
    }

    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        DataBase dataBase = protocol.getDataBase();

        if (!protocol.isLoggedIn() || dataBase.getUserNameToCustomer().get(userName) == null)
            return new ErrorMessage((short) 6); //if the sender is not logged in or the receiver is unRegistered we send error
        else {
            Customer otherCustomer = dataBase.getUserNameToCustomer().get(userName);
            short postNum = (short) otherCustomer.getNumOfPost();
            short numOfFollowers = (short) otherCustomer.getNumOfFollowers();
            short numOfFollowing = (short) otherCustomer.getNumOfFollowing();
            byte[] array1 = shortToBytes(postNum);
            byte[] array2 = shortToBytes(numOfFollowers);
            byte[] array3 = shortToBytes(numOfFollowing);
            byte[] optionalByteArray = new byte[6];
            optionalByteArray[0] = array1[0]; // fill the optionalByteArray with the bytes represented the needed shorts
            optionalByteArray[1] = array1[1];
            optionalByteArray[2] = array2[0];
            optionalByteArray[3] = array2[1];
            optionalByteArray[4] = array3[0];
            optionalByteArray[5] = array3[1];
            return new AckMessage((short) 8, optionalByteArray);
        }
    }


}
