package by.university.demo.dao;

import by.university.demo.entity.Role;
import by.university.demo.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserDao extends AbstractJDBCDao<User> {
    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM users join user_role on users.id = user_role.user_id";  // JOIN coursework.user_role ON users.id = user_role.user_id
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO users (email, password, username) values('%s', '%s', '%s');";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE users SET email = '%s', password = '%s', username = '%s' WHERE id = %s";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM users WHERE id = %d";
    }

    @Override
    protected List<User> parseResultSet(ResultSet resultSet) {
        List<User> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                if (users.size() > 0 && users.get(users.size() - 1).getId() == resultSet.getLong("id")) {
                    users.get(users.size() - 1).getRoles().add(Role.valueOf(resultSet.getString("roles")));
                } else {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));

                    Set<Role> roles = new HashSet<>();
                    roles.add(Role.valueOf(resultSet.getString("roles")));
                    user.setRoles(roles);

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    protected String statementUpdate(String sql, User user) {
        return String.format(sql, user.getEmail(), user.getPassword(), user.getUsername(), user.getId());
    }

    @Override
    protected String statementInsert(String sql, User user) {
        return String.format(sql, user.getEmail(), user.getPassword(), user.getUsername());
    }

    @Override
    protected String statementDelete(String sql, Long id) {
        return null;
    }

    public boolean login(String username, String password){
        String sql = getSelectQuery();
        sql += String.format(" where username = '%s' and password = '%s'", username, password);
        try (Statement statement = connectionDao.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDao.closeConnection();
        }
        return false;
    }

    public String generate(User user){
        String sql = getInsertQuery();
        try (Statement statement = connectionDao.getConnection().createStatement()) {
            sql = statementInsert(sql, user);
            statement.executeUpdate(sql);
            return sql;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDao.closeConnection();
        }
        return "";
    }
}
