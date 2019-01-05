package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import javafx.util.Pair;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class PostMessage extends Message {
    private short opcode= 5;
    private String content;
    private List<String> userToPost = new Vector<>(); //userList that have been tagged in the post

    public PostMessage(byte[] bytes) {
        super((short) 5);
        content = getContent(bytes);

        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '@') {
                i++;
                String userName = "";
                while (i != content.length() && content.charAt(i) != ' ') {  //the name is not finished
                    userName += content.charAt(i);  // we append the userName with the next byte.
                    i++;
                }
                userToPost.add(userName);  //add the name to the userList to post
            }
        }
    }

    private String getContent(byte[] messageBytesArray){
        String toReturn;
        int index = 2;
        int contentArraySize = 0;
        while (messageBytesArray[index] != '\0'){
            contentArraySize++;
            index++;
        }
        byte[] wordInByte= new byte[contentArraySize];
        index = 2;
        for (int i = 0; i <wordInByte.length; i++) {
            wordInByte[i] = messageBytesArray[index];
            index++;
        }
        toReturn = new String(wordInByte);
        return toReturn;
    }

    @Override
    public Message act(DataBase dataBase, Customer customer) {
        if (customer.isLoggedIn()) { // if customer is logged in
            customer.addPost(content); // first we save the content of the new post to the dataBase
            NotificationMessage notificationMessage = new NotificationMessage((byte)1, customer.getUserName(), content);
            List<Customer> followingMe = customer.getFollowingMe(); // we get the "who is following me" Vector
            // we iterate //todo: Check if there might be a problem when iterating over the vector when some one else is try to follow me at the same time.
            Vector<Customer> customersToSendNotificationToVector = new Vector<>(followingMe);
            for (String userNameToSendNotificationTo : userToPost) { // we iterate //todo: Check if there might be a problem when iterating over the vector when some one else is try to follow me at the same time.
                Customer customerToSendNotificationTo = dataBase.getUserNameToCustomer().get(userNameToSendNotificationTo);
                if (!customersToSendNotificationToVector.contains(customerToSendNotificationTo)) // we want to add him to the vector only if he is not already there. because we dont want someone to get two notifications.
                    customersToSendNotificationToVector.add(customerToSendNotificationTo);
            }
            ConcurrentHashMap<String, Pair<NotificationMessage, Vector<Customer>>> userNameToNotificationSendList = dataBase.getUserNameToNotificationSendList(); // we get the map where we will put the customer that should be notified
            userNameToNotificationSendList.put(customer.getUserName(), new Pair<>(notificationMessage, customersToSendNotificationToVector));
            return new AckMessage((short) 5, null);
        }
        return new ErrorMessage((short) 5);
        //todo: remember to check if user is registered before sending message
    }
}
