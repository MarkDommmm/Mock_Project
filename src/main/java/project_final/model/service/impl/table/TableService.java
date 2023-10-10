package project_final.model.service.impl.table;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;
import project_final.model.entity.Tables;
import project_final.model.repository.ITableRepository;
import project_final.model.service.mapper.table.ITableMapper;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TableService implements ITableService {
    private final ITableRepository tableRepository;
    private final ITableMapper tableMapper;

    @Override
    public Page<TableResponse> findAll(int page, int size) {
        Page<Tables> tables = tableRepository.findAll(PageRequest.of(page, size));
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
