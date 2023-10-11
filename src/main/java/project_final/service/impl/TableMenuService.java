package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project_final.entity.TableMenu;
import project_final.model.dto.request.TableMenuRequest;
import project_final.model.dto.response.TableMenuResponse;
import project_final.repository.ITableMenuRepository;
import project_final.service.ITableMenuService;
import project_final.service.mapper.ITableMenuMapper;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TableMenuService implements ITableMenuService {
    private final ITableMenuRepository tableMenuRepository;
    private final ITableMenuMapper tableMenuMapper;

    @Override
    public Page<TableMenuResponse> findAll(String name, int page, int size) {
        Page<TableMenu> tableMenus = tableMenuRepository.findAllByMenuName(name, PageRequest.of(page, size));
        return tableMenus.map(tableMenuMapper::toResponse);
    }

    @Override
    public TableMenuResponse findById(Long id) {
        Optional<TableMenu> tableMenu = tableMenuRepository.findById(id);
        if (tableMenu.isPresent()) {
            return tableMenuMapper.toResponse(tableMenu.get());
        }
        return null;
    }

    @Override
    public void save(TableMenuRequest tableMenuRequest) {
        tableMenuRepository.save(tableMenuMapper.toEntity(tableMenuRequest));
    }

    @Override
    public void delete(Long id) {
        Optional<TableMenu> tableMenu = tableMenuRepository.findById(id);
        if (tableMenu.isPresent()) {
            tableMenuRepository.deleteById(id);
        }
    }
}
