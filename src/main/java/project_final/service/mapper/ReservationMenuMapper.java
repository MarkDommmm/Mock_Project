package project_final.service.mapper;

import org.springframework.stereotype.Component;
import project_final.entity.ReservationMenu;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.ReservationMenuResponse;;

@Component
public class ReservationMenuMapper implements IReservationMenuMapper {
    @Override
    public ReservationMenu toEntity(ReservationMenuRequest tableMenuRequest) {
        Status pay;
        if (tableMenuRequest.getPay() == null) {
            pay = Status.UN_PAID;
        } else {
            pay = tableMenuRequest.getPay();
        }
        return ReservationMenu.builder()
                .id(tableMenuRequest.getId())
                .menu(tableMenuRequest.getMenu())
                .reservation(tableMenuRequest.getReservation())
                .quantity(tableMenuRequest.getQuantity())
                .price(tableMenuRequest.getMenu().getPrice())
                .status(Status.NOT_SERVED)
                .pay(pay).build();
    }

    @Override
    public TableMenuCartResponse toResponse(ReservationMenu reservationMenu) {
        return TableMenuCartResponse.builder()
                .id(reservationMenu.getId())
                .image(reservationMenu.getMenu().getImage())
                .name(reservationMenu.getMenu().getName())
                .quantity(reservationMenu.getQuantity())
                .price(reservationMenu.getPrice())
                .dateBooking(reservationMenu.getReservation().getBookingDate())
                .startTime(reservationMenu.getReservation().getStartTime())
                .endTime(reservationMenu.getReservation().getEndTime())
                .status(reservationMenu.getStatus())
                .pay(reservationMenu.getPay()).build();
    }

    @Override
    public ReservationMenuResponse toReponse(ReservationMenu reservationMenu) {
        return ReservationMenuResponse.builder()
                .id(reservationMenu.getId())
                .menu(reservationMenu.getMenu())
                .reservation(reservationMenu.getReservation())
                .quantity(reservationMenu.getQuantity())
                .price(reservationMenu.getPrice())
                .status(reservationMenu.getStatus())
                .pay(reservationMenu.getPay()).build();
    }
}
