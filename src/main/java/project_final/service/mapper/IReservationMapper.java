package project_final.service.mapper;

import project_final.entity.Reservation;
import project_final.model.dto.request.ReservationRequest;
import project_final.model.dto.response.ReservationCheckCodeResponse;
import project_final.model.dto.response.ReservationResponse;

public interface IReservationMapper extends IGenericMapper<Reservation, ReservationRequest, ReservationResponse>{
    ReservationCheckCodeResponse toResponseCheckCode(Reservation reservation);
}
