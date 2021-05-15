package by.university.demo.dao;

import by.university.demo.entity.Message;
import by.university.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MessageDao extends AbstractJDBCDao<Message> {
    @Autowired
    private UserRepository userRepository;
    @Override
    protected String getSelectQuery() {
        return "select * from message";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO message (tag, text, user_id) values('%s',' %s', %d)";
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM message WHERE id = %d";
    }

    @Override
    protected List<Message> parseResultSet(ResultSet resultSet) {
        List<Message> messages = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getLong(1));
                message.setTag(resultSet.getString(2));
                message.setText(resultSet.getString(3));
                message.setAuthor(userRepository.findById(resultSet.getLong(4)).get());

                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    protected String statementUpdate(String sql, Message object) {
        return null;
    }

    @Override
    protected String statementInsert(String sql, Message object) {
        return String.format(sql, object.getTag(), object.getText(), object.getAuthor().getId());
    }

    @Override
    protected String statementDelete(String sql, Long id) {
        return String.format(sql, id);
    }
}
