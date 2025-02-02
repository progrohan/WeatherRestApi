package com.progrohan.weather.repository;

import com.progrohan.weather.exception.DataBaseException;
import com.progrohan.weather.exception.DataExistException;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public abstract class BaseRepository<ID extends Serializable, E >
        implements CrudRepository<ID, E> {

    private final Class<E> entityClass;

    protected final SessionFactory sessionFactory;

    @Override
    public E save(E entity){
        try(Session session = sessionFactory.openSession()){

            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();

            return entity;
        }catch(ConstraintViolationException e){
            throw new DataExistException("");
        }
        catch(Exception e){
            throw new DataBaseException("Problem with saving in database!");
        }
    }

    @Override
    public Optional<E> findById(ID id) {
        try(Session session = sessionFactory.openSession()){

            E e = session.find(entityClass, id);

            return Optional.ofNullable(e);
        }catch(Exception e){
            throw new DataBaseException("Problem with finding by id in database!");
        }
    }

    @Override
    public void delete(ID id) {
        try(Session session = sessionFactory.openSession()){

            E e = session.find(entityClass, id);

            session.beginTransaction();
            session.remove(e);
            session.getTransaction().commit();

        }catch(Exception e){
            throw new DataBaseException("Problem with deleting from database!");
        }
    }

    @Override
    public E update(E entity) {
        try(Session session = sessionFactory.openSession()){

            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();

            return entity;
        }catch(Exception e){
            throw new DataBaseException("Problem with updating in database!");
        }
    }

    @Override
    public List<E> findAll() {
        try (Session session = sessionFactory.openSession()) {

            CriteriaQuery<E> criteria = session.getCriteriaBuilder().createQuery(entityClass);
            criteria.from(entityClass);

            return session.createQuery(criteria).getResultList();
        }catch(Exception e){
            throw new DataBaseException("Problem with selecting all from database!");
        }
    }
}