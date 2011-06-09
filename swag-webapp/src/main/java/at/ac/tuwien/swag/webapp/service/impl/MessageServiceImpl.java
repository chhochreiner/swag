package at.ac.tuwien.swag.webapp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import at.ac.tuwien.swag.model.dao.MessageDAO;
import at.ac.tuwien.swag.model.dao.UserDAO;
import at.ac.tuwien.swag.model.domain.Message;
import at.ac.tuwien.swag.model.domain.User;
import at.ac.tuwien.swag.model.dto.MessageDTO;
import at.ac.tuwien.swag.model.dto.UserDTO;
import at.ac.tuwien.swag.webapp.service.MessageService;

import com.google.inject.Inject;

public class MessageServiceImpl implements MessageService {

    private MessageDAO messages;
    private UserDAO users;

    @Inject
    public MessageServiceImpl(EntityManager em) {
        messages = new MessageDAO(em);
        users = new UserDAO(em);
    }

    @Override
    public List<MessageDTO> getInMessages(String username) {
        User user = users.findByUsername(username);

        Set<Message> inMessages = user.getReceivedMessages();
        List<MessageDTO> dtos = new ArrayList<MessageDTO>(inMessages.size());

        for (Message m : inMessages) {
            dtos.add(
                new MessageDTO(
                    m.getTimestamp(),
                    m.getSubject(),
                    "",
                    m.getRead(),
                    new UserDTO(
                        m.getFrom().getUsername(),
                        "",
                        "",
                        "",
                        "",
                        null,
                        null,
                        null
                    ),
                    null
                )
                );
        }

        return dtos;
    }

    @Override
    public List<MessageDTO> getOutMessages(String username) {
        User user = users.findByUsername(username);

        List<Message> outMessages = user.getSentMessages();
        List<MessageDTO> dtos = new ArrayList<MessageDTO>(outMessages.size());

        for (Message m : outMessages) {
            dtos.add(
                new MessageDTO(
                    m.getTimestamp(),
                    m.getSubject(),
                    "",
                    m.getRead(),
                    new UserDTO(
                        m.getFrom().getUsername(),
                        "",
                        "",
                        "",
                        "",
                        null,
                        null,
                        null
                    ),
                    null
                )
                );
        }

        return dtos;
    }

    @Override
    public List<MessageDTO> getNotifications(String user) {
        String query =
            "SELECT m FROM Message m LEFT JOIN FETCH m.from LEFT JOIN FETCH m.to AS y WHERE y.username = :username AND m.from.username = 'system'";

        Map<String, String> values = new HashMap<String, String>();
        values.put("username", user);

        List<Message> inMessages = messages.findByQuery(query, values);
        List<MessageDTO> result = new ArrayList<MessageDTO>();

        for (Message m : inMessages) {
            result.add(
                new MessageDTO(
                    m.getTimestamp(),
                    m.getSubject(),
                    "",
                    m.getRead(),
                    new UserDTO(
                        m.getFrom().getUsername(),
                        "",
                        "",
                        "",
                        "",
                        null,
                        null,
                        null
                    ),
                    null
                )
                );
        }

        return result;
    }

    @Override
    public MessageDTO getMessagebyId(Long id, String user) {

        String query =
            "SELECT m FROM Message m LEFT JOIN FETCH m.from LEFT JOIN FETCH m.to WHERE m.id = :number";

        Map<String, String> values = new HashMap<String, String>();
        values.put("number", id.toString());

        List<Message> message = messages.findByQuery(query, values);

        if (message.isEmpty()) {
            return null;
        }

        Message m = message.get(0);

        return new MessageDTO(
            m.getTimestamp(),
            m.getSubject(),
            m.getText(),
            m.getRead(),
            new UserDTO(
                m.getFrom().getUsername(),
                "",
                "",
                "",
                "",
                null,
                null,
                null
            ),
            null);
    }

    @Override
    public void sendMessage(String subject, String text, Set<String> reciever, String sender) {

        Message message = new Message();
        message.setTimestamp(new Date());
        message.setSubject(subject);
        message.setText(text);
        message.setRead(false);

        User senderUser = getUserWithMessages(sender);

        message.setFrom(senderUser);

        List<Message> sentMessages = new ArrayList<Message>();
        sentMessages.addAll(senderUser.getSentMessages());
        sentMessages.add(message);
        senderUser.setSentMessages(sentMessages);

        Set<User> recieverAsUser = new HashSet<User>();
        for (String rec : reciever) {
            recieverAsUser.add(getUserWithMessages(rec));
        }

        message.setTo(recieverAsUser);

        messages.beginTransaction();
        // users.update(senderUser);
        //
        // for (User u : recieverAsUser) {
        // Set<Message> recievedMessages = new HashSet<Message>();
        // recievedMessages.addAll(u.getReceivedMessages());
        // recievedMessages.add(message);
        // u.setReceivedMessages(recievedMessages);
        // users.update(u);
        // }

        messages.insert(message);

        messages.commitTransaction();

    }

    @Override
    public void sendNotification(String subject, String text, String reciever) {

        checkPostmaster();

        Message message = new Message();
        message.setTimestamp(new Date());
        message.setSubject(subject);
        message.setText(text);
        message.setRead(false);
        message.setFrom(users.findByUsername("postmaster"));

        Set<User> recieverAsUser = new HashSet<User>();
        recieverAsUser.add(users.findByUsername(reciever));

        message.setTo(recieverAsUser);

        messages.insert(message);

        // TODO check online status and send mails
        // TODO set transaction
    }

    private void checkPostmaster() {
        // create postmaster aka root oder so

        try {
            users.findByUsername("postmaster");
        } catch (NoResultException e) {
            users.beginTransaction();
            User user = new User();
            user.setUsername("postmaster");
            users.insert(user);
            users.commitTransaction();
        }
    }

    @Override
    public void updateReadStatus(Long id) {

        Message message = messages.findById(id);
        message.setRead(true);
        messages.update(message);

    }

    private User getUserWithMessages(String name) {
        String query =
            "SELECT u FROM User u LEFT JOIN FETCH u.sentMessages LEFT JOIN FETCH u.receivedMessages WHERE u.username = :username";

        Map<String, String> values = new HashMap<String, String>();
        values.put("username", name);

        List<User> message = users.findByQuery(query, values);

        return message.get(0);
    }

}
