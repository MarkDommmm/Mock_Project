package project_final.service;

import org.springframework.data.domain.Page;
import project_final.entity.Reservation;
import project_final.model.dto.request.TableMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.TableMenuResponse;

import java.util.List;


public interface ITableMenuService extends IGenericService<TableMenuRequest, TableMenuCartResponse, Long> {
    Page<TableMenuResponse> getAll(Long id, int page, int size);

    Page<TableMenuCartResponse> getTableMenu(Long id, int page, int size);

    List<TableMenuCartResponse> getDetails(Long id);

    Reservation addCart(Long id, Long idUser, Long idTable);

    void removeCartItem(Long id);

    void changeStatus(Long id);
}