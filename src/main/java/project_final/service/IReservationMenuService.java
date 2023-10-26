package project_final.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project_final.entity.Reservation;
import project_final.entity.ReservationMenu;
import project_final.model.dto.request.ReservationMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.ReservationMenuResponse;

import java.util.List;


public interface IReservationMenuService extends IGenericService<ReservationMenuRequest, TableMenuCartResponse, Long> {
    Page<TableMenuCartResponse> getAll(Long id, int page, int size);

    Page<TableMenuCartResponse> getTableMenu(Long id, int page, int size);
    Page<ReservationMenuResponse> getReservationMenu(Long id, int page, int size);

    List<TableMenuCartResponse> getDetails(Long id);
    ReservationMenu served(int quantity,Long id);

    String adminCancel(Long id);

    void addCart(Long id, Long idR);

    void removeCartItem(Long id);


}