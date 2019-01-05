package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class FollowMessage extends Message {
    private short opcode= 4;
    private Boolean follow; //(true=follow, false= unFollow)
    private short numOfUsers;
    private List<String> userNameList;

    public FollowMessage(byte[] messageBytesArray) {
        super((short)4);
        userNameList = new Vector<>();
        if (messageBytesArray[2] == 0) follow = true;
        else follow = false;
        byte[] twoBytes = {messageBytesArray[3], messageBytesArray[4]};
        numOfUsers = bytesToShort(twoBytes);
        int index = 5;
        while (index < messageBytesArray.length&& userNameList.size()<numOfUsers) {
         //   byte[] nameBytes;
            String userName = "";
            int counterForNameBytes=0;
            int primeIndex= index;
            while (messageBytesArray[index] != '\0') { //counting num of bytes for the userName
                counterForNameBytes++;
                index++;
            }
            byte [] nameBytes= new byte [counterForNameBytes];
            for (int i = 0; i < counterForNameBytes ; i++) {
                nameBytes[i]= messageBytesArray[primeIndex];
                primeIndex++;
            }
            userName= new String(nameBytes);
            userNameList.add(userName); //when word finished (\0) - we add the userName to the list
            index++;// pass /0 byte
        }
    }

    @Override
    public Message act(BidiMessagingProtocolImpl protocol) {
        List<String> successList = new Vector<>();
        Customer thisCustomer = protocol.getCustomer();
        DataBase dataBase = protocol.getDataBase();
        short successNum = 0;
        if (thisCustomer.isLoggedIn()) {
            for (String userName : userNameList) { // follow/ unFollow each customer in the list
                Customer otherCustomer = dataBase.getUserNameToCustomer().get(userName); // customer from the list to un/follow
                if (otherCustomer != null) { //the user with this name is register
                    if (follow) {
                        if (!thisCustomer.getFollowedByMe().contains(otherCustomer)) { //if I'm not already following otherCustomer.
                            thisCustomer.addFollowing(otherCustomer);
                            otherCustomer.addFollower(thisCustomer);
                            successNum++;
                            successList.add(otherCustomer.getUserName());
                        }
                    } else { //unFollow
                        if (thisCustomer.getFollowedByMe().contains(otherCustomer)) { //if I'm following otherCustomer.
                            thisCustomer.removeFollowing(otherCustomer);
                            otherCustomer.removeFollower(thisCustomer);
                            successNum++;
                            successList.add(otherCustomer.getUserName());
                        }
                    }
                }
            }
        }
        if (successNum > 0) { // if we followed/unfollowed at least 1 person.
            byte[] numOfUsersBytesArr = shortToBytes(successNum);

            String userNameListBytesArr = "";
            for (String name : successList) {
                userNameListBytesArr += (name + "\0"); //\0 will be separating 0 byte
            }
            byte[] ackMessageOptionalArr = merge2Arrays(numOfUsersBytesArr, userNameListBytesArr.getBytes());

            return new AckMessage((short) 4, ackMessageOptionalArr);
        } else return new ErrorMessage((short) 4);
    }

}
