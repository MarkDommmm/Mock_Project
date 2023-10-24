package project_final.service;

import org.springframework.data.domain.Page;
import project_final.entity.Reservation;
import project_final.entity.User;
import project_final.exception.TimeIsValidException;
import project_final.model.dto.response.ReservationCheckCodeResponse;
import project_final.model.dto.response.ReservationResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReservationService<K,V,E> {

    Page<V> findByUserIdAndStatusPending( int page, int size,Long id);

    Page<V> findByUserId( int page, int size,Long id);
    Page<V> findAll(Date date, int page, int size);
    List<V> findAll();
    V findById(E id);
    List<V> findAllByStatusORDER();
    Reservation add(Long user,Date date,String start,String end,Long idTable) throws TimeIsValidException;
    void save(K k )  throws TimeIsValidException;
    void confirm(E id);
    void changeStatusOrder(E id);
    String cancel(E id, Long idUser);
    void completed(E id);
    void noShow(E id);
    ReservationCheckCodeResponse findByCode(String code);
    double revenuesOnDay( Date date);
    int countCompletedReservationsOnDay(Date date);
    Page<Map<String,Object>>  findReservationStatistics(int page, int size);
}
