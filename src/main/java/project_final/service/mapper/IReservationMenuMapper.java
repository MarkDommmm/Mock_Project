package project_final.service.mapper;

import project_final.entity.ReservationMenu;
import project_final.model.dto.request.ReservationMenuRequest;
import project_final.model.dto.response.ReservationMenuResponse;
import project_final.model.dto.response.TableMenuCartResponse;

public interface IReservationMenuMapper extends IGenericMapper<ReservationMenu, ReservationMenuRequest, TableMenuCartResponse>{
    ReservationMenuResponse toReponse(ReservationMenu reservationMenu);
}
