package by.university.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJDBCDao<T> {
    @Autowired
    protected ConnectionDao connectionDao;

    protected AbstractJDBCDao(ConnectionDao connectionDao) {
        this.connectionDao = connectionDao;
    }

    protected AbstractJDBCDao() {
    }

    protected abstract String getSelectQuery();

    protected abstract String getInsertQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getDeleteQuery();

    protected abstract List<T> parseResultSet(ResultSet resultSet);

    protected abstract String statementUpdate(String sql, T object);

    protected abstract String statementInsert(String sql, T object);

    protected abstract String statementDelete(String sql, Long id);

    public List<T> getByKeyValue(String key, String value) {
        List<T> object = null;
        String sql = getSelectQuery();
        sql += String.format(" WHERE %s = '%s'", key, value);
        try (Statement statement = connectionDao.getConnection().createStatement()) {
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            object = parseResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDao.closeConnection();
        }
        return object;
    }

    public List<T> getByTwoValues(String key1, String key2, String v1, String v2){
        List<T> object = new ArrayList<>();
        String sql = getSelectQuery();
        sql += String.format(" where %s = '%s' and %s = '%s'", key1,v1,key2,v2);
        try (Statement statement = connectionDao.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            object = parseResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDao.closeConnection();
        }
        return object;
    }

    public List<T> findAll() {
        String sql = getSelectQuery();
        List<T> objects = new ArrayList<>();
        try (Statement statement = connectionDao.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            objects = parseResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDao.closeConnection();
        }
        return objects;
    }

    public void update(T object) {
        String sql = getUpdateQuery();
        try (Statement statement = connectionDao.getConnection().createStatement()) {
            sql = statementUpdate(sql, object);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDao.closeConnection();
        }
    }

    public void insert(T object) {
        String sql = getInsertQuery();
        try (Statement statement = connectionDao.getConnection().createStatement()) {
            sql = statementInsert(sql, object);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDao.closeConnection();
        }
    }

    public void deleteById(Long id){
        String sql = getDeleteQuery();
        try(Statement statement = connectionDao.getConnection().createStatement()) {
            sql = statementDelete(sql, id);
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
