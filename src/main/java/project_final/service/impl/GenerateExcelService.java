package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project_final.entity.Reservation;
import project_final.repository.IReservationRepository;
import project_final.util.ExcelUtil;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenerateExcelService {
    private final ExcelUtil excelUtil;
    private final IReservationRepository reservationRepository;

    public ByteArrayInputStream load(Long id) {
        Optional<Reservation> reservation =  reservationRepository.findById(id);
        ByteArrayInputStream inputStream = excelUtil.tutorialsToExcel(reservation.get());
        return inputStream;
    }
}
