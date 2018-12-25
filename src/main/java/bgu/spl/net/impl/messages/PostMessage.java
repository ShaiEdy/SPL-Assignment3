package bgu.spl.net.impl.messages;

import java.util.List;

public class PostMessage<T> extends Message<T> {
    String content;
    List<String> userToPost;

    public PostMessage(byte[] bytes) {
        int index=2;
        while (bytes[index]!='\0') { //while the content is'nt finished
            if (Byte.toString(bytes[index]).equals("@")) { //we deal with @userName
                String userName= "";
                while (!Byte.toString(bytes[index]).equals(" ")){
                    userName+= Byte.toString((bytes[index]));  // we append the userName with the next byte.
                    content += Byte.toString(bytes[index]); // we append the content with the next byte.
                    index++;
                }
                content += Byte.toString(bytes[index]); // we append the content with the next byte-" ".
                index++;
                userToPost.add(userName); //add the name to the userList to post
            }
            else{
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
