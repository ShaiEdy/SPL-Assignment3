package bgu.spl.net.impl.messages;

import java.util.List;

public class PostMessage<T> extends Message<T> {
    String content;
    List<String> userToPost; //userList that have been tagged in the post

    public PostMessage(byte[] bytes) {
        int index = 2;
        while (index < bytes.length) { //while the content is'nt finished
            if (Byte.toString(bytes[index]).equals("@")) { //we deal with @userName
                String userName = "";
                while (index < bytes.length && !Byte.toString(bytes[index]).equals(" ")) {  //the name is not finished
                    userName += Byte.toString((bytes[index]));  // we append the userName with the next byte.
                    index++;
                }
                userToPost.add(userName); //add the name to the userList to post
                content += userName; // we append the content with the userName
                if (index < bytes.length) content += " "; // add " " after the userName in the content (I assume that the userName ends with space or nothing.)
                index++;
            } else {
                content += Byte.toString(bytes[index]); // we append the content with the next byte.
                index++;
            }
        }
    }

    @Override
    public T act(T message) {
        return null;
    }
}
