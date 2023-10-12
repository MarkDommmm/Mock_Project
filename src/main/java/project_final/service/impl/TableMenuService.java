package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.Menu;
import project_final.entity.TableMenu;
import project_final.model.dto.request.TableMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.TableMenuResponse;
import project_final.repository.IMenuRepository;
import project_final.repository.ITableMenuRepository;
import project_final.service.ITableMenuService;
import project_final.service.mapper.CartMapper;
import project_final.service.mapper.ITableMenuMapper;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TableMenuService implements ITableMenuService {
    private final ITableMenuRepository tableMenuRepository;
    private final ITableMenuMapper tableMenuMapper;
    private final IMenuRepository menuRepository;

    @Override
    public Page<TableMenuCartResponse> findAll(String name, int page, int size) {
        Page<TableMenu> tableMenus = tableMenuRepository.findAllByMenuName(
                name, PageRequest.of(page, size));
        return tableMenus.map(tableMenuMapper::toResponse);
    }

    @Override
    public Page<TableMenuResponse> getAll(String name, int page, int size) {
        Page<TableMenu> tableMenus = tableMenuRepository.findAllByMenuName(
                name, PageRequest.of(page, size));
        return tableMenus.map(tableMenuMapper::toRepon);
    }

    @Override
    public void addCart(Long id) {
        Optional<Menu> menuOptional = menuRepository.findById(id);

        if (menuOptional.isPresent()) {
            Menu menu = menuOptional.get();
            List<TableMenu> tableMenuCartResponses = tableMenuRepository.findAll();
            boolean menuInCart = false;
            for (TableMenu tableMenu : tableMenuCartResponses) {
                if (tableMenu.getMenu().getId().equals(menu.getId())) {
                    tableMenu.setQuantity(tableMenu.getQuantity() + 1);
                    tableMenu.setPrice(tableMenu.getMenu().getPrice() * tableMenu.getQuantity());
                    menuInCart = true;
                    tableMenuRepository.save(tableMenu);
                    break;
                }
            }
            if (!menuInCart) {
                TableMenuRequest tableMenuRequest = new TableMenuRequest();
                tableMenuRequest.setMenu(menu);
                tableMenuRequest.setPrice(menu.getPrice());
                save(tableMenuRequest);
            }
        }
    }



    @Override
    public TableMenuCartResponse findById(Long id) {
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
