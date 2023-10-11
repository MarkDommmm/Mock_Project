package project_final.service;

import org.springframework.data.domain.Page;
import project_final.entity.User;

import java.util.Date;

public interface IReservationService<K,V,E> {
    Page<V> findAll(Date date, int page, int size);
    V findById(E id);
    void save(K k);
    void confirm(E id);
    void cancel(E id, User user);
}
