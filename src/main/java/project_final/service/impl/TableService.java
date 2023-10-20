package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.exception.CustomsException;
import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;
import project_final.entity.Tables;
import project_final.repository.ITableRepository;
import project_final.service.ITableService;
import project_final.service.mapper.ITableMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TableService implements ITableService {
    private final ITableRepository tableRepository;
    private final ITableMapper tableMapper;

    public List<TableResponse> getTables(String name) {
        List<Tables> tables = tableRepository.findAllByTableTypeName(name);
        return tables.stream().map(tableMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Tables findByIdTable(Long id) {
        Optional<Tables> table = tableRepository.findById(id);
        return table.get();
    }

    @Override
    public Page<TableResponse> findAllByStatusIsTrueAndName(String name, int page, int size) {
        Page<Tables> tables = tableRepository.findAllByStatusIsTrueAndName(name,PageRequest.of(page, size));
        return tables.map(tableMapper::toResponse);
    }

    @Override
    public Page<TableResponse> findAvailableTables(String name,Date date, String start, String end, int page, int size) {
        Page<Tables> tables = tableRepository.findAvailableTables(name,date, start,end,PageRequest.of(page, size));
        return tables.map(tableMapper::toResponse);
    }

    @Override
    public Page<Map<String, Object>> getTableStatusForDate(Date date, String startTime, String endTime, int page, int size) {
        Page<Map<String,Object>> map =tableRepository.getTableStatusForDate(date, startTime, endTime, PageRequest.of(page, size));
        return tableRepository.getTableStatusForDate(date, startTime, endTime, PageRequest.of(page, size));
    }

    @Override
    public void changeStatus(Long id) {
        Optional<Tables> table = tableRepository.findById(id);
        if (table.isPresent()) {
            table.get().setStatus(!table.get().isStatus());
            tableRepository.save(table.get());
        }
    }

    @Override
    public boolean isTableAvailable(Long tableId, Date bookingDate, String startTime, String endTime) {
        return tableRepository.isTableAvailable(tableId, bookingDate, startTime, endTime);
    }

    @Override
    public Page<TableResponse> findAll(String name, int page, int size) {
        Page<Tables> tables = tableRepository.findAllByNameContains(name, PageRequest.of(page, size));
        return tables.map(tableMapper::toResponse);
    }


    @Override
    public TableResponse findById(Long id) {
        Optional<Tables> table = tableRepository.findById(id);
        return table.map(tableMapper::toResponse).orElse(null);
    }

    @Override
    public void save(TableRequest tableRequest) {
        tableRepository.save(tableMapper.toEntity(tableRequest));
    }

    @Override
    public void delete(Long id) {
        Optional<Tables> table = tableRepository.findById(id);
        if (table.isPresent()) {
            tableRepository.deleteById(id);
        }
    }
}
