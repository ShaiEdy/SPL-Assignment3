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


    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    @Override
    protected Message act(DataBase dataBase, Customer thisCustomer) {
        List<String> successList = new Vector<>();
        Integer successNum = 0;
        if (thisCustomer.isLoggedIn()) {
            for (String userName : userNameList) { // follow/ unFollow each customer in the list
                Customer otherCustomer = dataBase.getUserNameToCustomer().get(userName); // customer from the list to un/follow
                if (otherCustomer != null) { //the user with this name is register
                    if (follow) {
                        if (!thisCustomer.getFollowedByMe().contains(otherCustomer)) { //if I'm not already following otherCustomer.
                            //if (!otherCustomer.getFollowingMe().contains(otherCustomer)) { //not already Follow
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
        if (successNum > 0) {
            String OptionalArray= successNum.toString();
            for (String name: successList){
                OptionalArray+=(name+"\0"); //\0 will be separating 0 byte
            }
            return new AckMessage((short) 4, OptionalArray.getBytes());
        } else return new ErrorMessage((short) 4);
    }
}
