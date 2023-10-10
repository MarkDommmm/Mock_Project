package project_final.model.service.impl.menu;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.model.dto.request.MenuRequest;
import project_final.model.dto.response.MenuResponse;
import project_final.model.entity.Menu;
import project_final.model.repository.IMenuRepository;
import project_final.model.service.mapper.menu.IMenuMapper;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuService implements IMenuService<MenuRequest, MenuResponse,Long>{

    private final IMenuRepository menuRepository;
    private final IMenuMapper menuMapper;

    @Override
    public Page<MenuResponse> findAll(String name, int page, int size) {
        Page<Menu> menus = menuRepository.findAllByNameContains(name, PageRequest.of(page, size));
        return menus.map(menuMapper::toResponse);
    }


    @Override
    public MenuResponse findById(Long id) {
        Optional<Menu> menu = menuRepository.findById(id);
        return menu.map(menuMapper::toResponse).orElse(null);
    }

    @Override
    public void save(MenuRequest menuRequest) {
        menuRepository.save(menuMapper.toEntity(menuRequest));
    }

    @Override
    public void delete(Long id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if(menu.isPresent()) {
            menuRepository.deleteById(id);
        }
    }
}
