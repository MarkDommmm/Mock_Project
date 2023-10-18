package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.exception.CustomsException;
import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.entity.TableType;
import project_final.repository.ITableTypeRepository;
import project_final.service.ITableTypeService;
import project_final.service.mapper.ITableTypeMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TableTypeService implements ITableTypeService {
    private final ITableTypeRepository tableTypeRepository;
    private final ITableTypeMapper iTableTypeMapper;


    @Override
    public TableTypeResponse findById(Long id) {
        Optional<TableType> tableType = tableTypeRepository.findById(id);
        return tableType.map(iTableTypeMapper::toResponse).orElse(null);
    }

    @Override
    public void save(TableTypeRequest tableTypeRequest)  {
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
        return  tableTypes.map(iTableTypeMapper::toResponse);
    }

    @Override
    public List<TableTypeResponse> findAll() {
        return tableTypeRepository.findAll().stream().map(iTableTypeMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<TableTypeResponse> findAllByStatusIsTrueAndName(String name, int page, int size) {
        Page<TableType> tableTypes = tableTypeRepository.findAllByStatusIsTrueAndName(name, PageRequest.of(page, size));
        return tableTypes.map(iTableTypeMapper::toResponse);
    }

    @Override
    public void changeStatus(Long id) {
        Optional<TableType> tableType = tableTypeRepository.findById(id);
        if (tableType.isPresent()) {
            tableType.get().setStatus(!tableType.get().isStatus());
            tableTypeRepository.save(tableType.get());
        }
    }
}
