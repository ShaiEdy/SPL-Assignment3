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
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer thiscustomer) {
        if (thiscustomer==null) return new ErrorMessage((short)4);
        else {
            for (String userName : userNameList) { // follow/ unFollow each customer in the list
                Customer customer = dataMap.get(userName); // customer from the list to un/follow
                if (customer != null) { //the user with this name is register
                    if (follow) customer.addFolloing(thiscustomer);
                    if (!follow) customer.removeFolloing(thiscustomer);
                }
            }
            return new AckMessage((short)4, null );
        }
    }
}