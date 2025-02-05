package com.progrohan.weather.repository;

import com.progrohan.weather.model.entity.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class SessionRepository extends BaseRepository<String, Session>{

    @Autowired
    public SessionRepository(SessionFactory sessionFactory) {
        super(Session.class, sessionFactory);
    }

}
