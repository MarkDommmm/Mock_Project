package project_final.model.service.impl.tableType;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.model.entity.TableType;
import project_final.model.repository.ITableTypeRepository;
import project_final.model.service.mapper.tableType.ITableTypeMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TableTypeService implements ITableTypeService {
    private final ITableTypeRepository tableTypeRepository;
    private final ITableTypeMapper iTableTypeMapper;

    @Override
    public Page<TableTypeResponse> findAll(int page, int size) {
        return null;
    }

    @Override
    public TableTypeResponse findById(Long id) {
        Optional<TableType> tableType = tableTypeRepository.findById(id);
        if (tableType.isPresent()) {
            return iTableTypeMapper.toResponse(tableType.get());
        }
        return null;
    }

    @Override
    public void save(TableTypeRequest tableTypeRequest) {
        tableTypeRepository.save(iTableTypeMapper.toEntity(tableTypeRequest));
    }

    @Override
    public void delete(Long id) {
        if (findById(id) != null) {
            tableTypeRepository.deleteById(id);
        }
    }

    @Override
    public Page<TableTypeResponse> findAll(String name, int page, int size) {
        Page<TableType> tableTypes = tableTypeRepository.findAllByNameContains(name, PageRequest.of(page, size));
        return  tableTypes.map(tableType-> iTableTypeMapper.toResponse(tableType));
    }

    @Override
    public List<TableTypeResponse> findAll() {

        return tableTypeRepository.findAll().stream().map(t->iTableTypeMapper.toResponse(t)).collect(Collectors.toList());
    }
}
