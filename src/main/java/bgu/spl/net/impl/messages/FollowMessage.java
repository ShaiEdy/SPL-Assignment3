package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class FollowMessage extends Message {
    private Boolean follow; //(true=follow, false= unFollow)
    private short numOfUsers;
    private List<String> userNameList;

    public FollowMessage(byte[] messageBytesArray) {
        userNameList = new Vector<>();
        if (messageBytesArray[2] == 0) follow = true;
        else follow = false;
        byte[] twoBytes = {messageBytesArray[3], messageBytesArray[4]};
        numOfUsers = bytesToShort(twoBytes);
        int index = 5;
        while (index < messageBytesArray.length) {
            String userName = "";
            while (messageBytesArray[index] != '\0') {
                userName += Byte.toString(messageBytesArray[index]); // we append the userName with the next byte.
                index++;
            }
            userNameList.add(userName); //when word finished (\0) - we add the userName to the list
            index++;
        }
    }

    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }


    @Override
    public Message act(DataBase dataBase, Customer thisCustomer) {
        List<String> successList = new Vector<>();
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

    private byte[] merge2Arrays(byte[] arr1, byte[] arr2) {
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
