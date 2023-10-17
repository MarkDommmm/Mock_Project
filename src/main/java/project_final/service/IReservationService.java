package project_final.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import project_final.entity.Reservation;
import project_final.entity.User;
import project_final.exception.TimeIsValidException;

import java.util.Date;
import java.util.List;

public interface IReservationService<K,V,E> {
    Page<V> findByUserId( int page, int size,Long id);
    Page<V> findAll(Date date, int page, int size);
    List<V> findAll();
    V findById(E id);
    void save(K k, Reservation reservation)  throws TimeIsValidException;
    void confirm(E id);
    void cancel(E id, User user);
    double revenuesOnDay( Date date);
    int countCompletedReservationsOnDay(Date date);
}
