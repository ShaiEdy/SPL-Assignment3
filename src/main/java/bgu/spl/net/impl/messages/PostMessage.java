package bgu.spl.net.impl.messages;

import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import javafx.util.Pair;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class PostMessage extends Message {
    private String content;
    private List<String> userToPost; //userList that have been tagged in the post

    public PostMessage(byte[] bytes) {
        int index = 2;
        while (bytes[index] != '\0') {
            //while (index < bytes.length) { //while the content is'nt finished
            if (Byte.toString(bytes[index]).equals("@")) { //we deal with @userName
                index++; // we skip the "@" itself.
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
    protected Message act(DataBase dataBase, Customer customer) {
        if (customer.isLoggedIn()) { // if customer is logged in
            customer.addPost(content); // first we save the content of the new post to the dataBase
            NotificationMessage notificationMessage = new NotificationMessage('1', customer.getUserName(), content);
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
