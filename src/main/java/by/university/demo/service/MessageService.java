package by.university.demo.service;

import by.university.demo.dao.MessageDao;
import by.university.demo.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageDao messageDao;

    @Autowired
    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public List<Message> findAll() {
        return messageDao.findAll();
    }

    public List<Message> findByTag(String tag) {
        return messageDao.getByKeyValue("tag", tag);
    }

    public void save(Message message) {
        messageDao.insert(message);
    }

    public void deleteById(Long id) {
        messageDao.deleteById(id);
    }
}
