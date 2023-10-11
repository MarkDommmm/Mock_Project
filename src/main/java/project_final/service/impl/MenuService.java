package project_final.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.model.dto.request.MenuRequest;
import project_final.model.dto.response.MenuResponse;
import project_final.entity.Menu;
import project_final.repository.IMenuRepository;
import project_final.service.IMenuService;
import project_final.service.mapper.IMenuMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MenuService implements IMenuService<MenuRequest, MenuResponse,Long> {

    private final IMenuRepository menuRepository;
    private final IMenuMapper menuMapper;

    @Override
    public Page<MenuResponse> findAll(String name, int page, int size) {
        Page<Menu> menus = menuRepository.findAllByNameContains(name, PageRequest.of(page, size));
        return menus.map(menuMapper::toResponse);
    }

    @Override
    public List<MenuResponse> findTopSellingMenus() {
        return menuRepository.findTopSellingMenus().stream().map(menuMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<MenuResponse> findAllByCategoryName(String category, int page, int size) {
        Page<Menu> menus = menuRepository.findAllByCategoryName(category, PageRequest.of(page, size));
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
