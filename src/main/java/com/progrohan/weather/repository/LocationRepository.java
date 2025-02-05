package com.progrohan.weather.repository;

import com.progrohan.weather.exception.DataBaseException;
import com.progrohan.weather.model.entity.Location;
import com.progrohan.weather.model.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepository extends BaseRepository<Long, Location>{

    @Autowired
    public LocationRepository(SessionFactory sessionFactory){
        super(Location.class, sessionFactory);
    }

    public List<Location> findByUserID(User user){
        try(Session session = sessionFactory.openSession()){
            List<Location> locations = session
                    .createQuery("FROM Location WHERE userId = :userId", Location.class)
                    .setParameter("userId", user)
                    .getResultList();
            return locations;
        }catch (Exception e){
            throw new DataBaseException("Problem with finding location by users Id!");
        }
    }

}
