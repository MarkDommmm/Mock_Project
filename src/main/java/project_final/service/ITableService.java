package project_final.service;


import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import project_final.entity.Tables;
import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ITableService extends IGenericService<TableRequest, TableResponse, Long> {
    List<TableResponse> getTables(String name);

    Tables findByIdTable(Long id);

    Page<TableResponse> findAllByStatusIsTrueAndName(String name, int page, int size);

    Page<TableResponse> findAvailableTables(String name, Date date, String start, String end, int page, int size);

    Page<Map<String, Object>> getTableStatusForDate(Date date, String startTime, String endTime, int page, int size);

    void changeStatus(Long id);

    boolean isTableAvailable(Long tableId, Date bookingDate, String startTime, String endTime);
}
