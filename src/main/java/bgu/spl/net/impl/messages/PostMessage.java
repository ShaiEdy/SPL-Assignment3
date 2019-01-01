package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PostMessage extends Message {
    String content;
    List<String> userToPost; //userList that have been tagged in the post

    public PostMessage(byte[] bytes) {
        int index = 2;
        while (bytes[index] != '\0') {
            //while (index < bytes.length) { //while the content is'nt finished
            if (Byte.toString(bytes[index]).equals("@")) { //we deal with @userName
                String userName = "";
                while (bytes[index] != '\0' && !Byte.toString(bytes[index]).equals(" ")) {  //the name is not finished
                    userName += Byte.toString((bytes[index]));  // we append the userName with the next byte.
                    index++;
                }
                userToPost.add(userName); //add the name to the userList to post
                content += userName; // we append the content with the userName
                if (bytes[index] != '\0')
                    content += " "; // add " " after the userName in the content (I assume that the userName ends with space or nothing.)
                index++;
            } else {
                content += Byte.toString(bytes[index]); // we append the content with the next byte.
                index++;
            }
        }
    }

    @Override
    protected Message act(ConcurrentHashMap<String, Customer> dataMap, Customer customer) {
        //todo: remember to check if user is registered before sending message

    return null;
    }
}
