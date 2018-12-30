package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

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
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer thisCustomer) {
        List<String> successList = new Vector();
        int successNum = 0;
        if (thisCustomer != null && thisCustomer.isLogin()) {
            for (String userName : userNameList) { // follow/ unFollow each customer in the list
                Customer otherCustomer = dataMap.get(userName); // customer from the list to un/follow
                if (otherCustomer != null) { //the user with this name is register
                    if (follow) {
                        if (!otherCustomer.getFollowing().contains(otherCustomer)) { //not already Follow
                            otherCustomer.addFollowing(thisCustomer);
                            successNum++;
                            successList.add(otherCustomer.getUserName());
                        }
                    } else { //unFollow
                        otherCustomer.removeFollowing(thisCustomer);
                        if (otherCustomer.getFollowing().contains(otherCustomer)) { //if is now follow we will make it unFollow
                            otherCustomer.removeFollowing(thisCustomer);
                            successNum++;
                            successList.add(otherCustomer.getUserName());
                        }
                    }

                }
            }
        }
        if (successNum > 0) {
            String namesForOptionalArray= "";
            for (String name: successList){
                namesForOptionalArray+=("0"+name);
            }
            return new AckMessage((short) 4, null);
        } else return new ErrorMessage((short) 4);
    }
}
