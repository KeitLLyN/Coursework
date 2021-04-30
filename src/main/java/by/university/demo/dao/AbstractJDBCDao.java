package by.university.demo.dao;

import by.university.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

public class AbstractJDBCDao {
    EntityManagerFactory em = Persistence.createEntityManagerFactory("by.university.demo.entity.User");

    public List<User> test(String id) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        String jql = "FROM User WHERE id = '" + id + "'";
        TypedQuery<User> query = entityManager.createQuery(jql, User.class);
        List<User> resultList = query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return resultList;
    }

    public EntityManager getEntityManager(){
        return em.createEntityManager();
    }

}
