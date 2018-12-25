package bgu.spl.net.impl.messages;

import java.util.List;
import java.util.Vector;

public class FollowMessage<T> extends Message<T> {
    private Boolean follow; //(true=follow, false= unFollow)
    private int numOfUsers;
    private List<String> userNameList;

    public FollowMessage(byte[] messageBytesArray) {
        userNameList= new Vector<>();
        if (messageBytesArray[2]==0) follow=true;
        else follow=false;
        numOfUsers= messageBytesArray[3];
        int index=4;

        while (index<messageBytesArray.length) {
            String userName= "";
            while (messageBytesArray[index] != '\0') {
                userName += Byte.toString(messageBytesArray[index]); // we append the userName with the next byte.
                index++;
            }
            userNameList.add(userName); //when word finished (\0) - we add the userName to the list
            index++;
        }
    }

    @Override
    public T act(T message) {
        return null;
    }  //make it follow or unFollow and return the result (success or not)
}
