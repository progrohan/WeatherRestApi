package com.progrohan.weather.repository;

import com.progrohan.weather.exception.DataBaseException;
import com.progrohan.weather.model.entity.User;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<Long, User>{

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    public Optional<User> findByName(String name){
        try(Session session = sessionFactory.openSession()){
            User user = session
                    .createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", name)
                    .getSingleResult();
            return Optional.of(user);
        }catch (NoResultException e){
            return Optional.empty();
        }catch (Exception e){
            throw new DataBaseException("Problem with finding user by name!");
        }
    }

}
