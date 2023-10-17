package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.*;
import project_final.model.domain.Status;
import project_final.model.dto.request.TableMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.TableMenuResponse;
import project_final.repository.*;
import project_final.service.ITableMenuService;
import project_final.service.mapper.CartMapper;
import project_final.service.mapper.ITableMenuMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TableMenuService implements ITableMenuService {
    private final ITableMenuRepository tableMenuRepository;
    private final ITableMenuMapper tableMenuMapper;
    private final IMenuRepository menuRepository;
    private final ITableRepository tableRepository;
    private final IUserRepository userRepository;
    private final IReservationRepository reservationRepository;

    @Override
    public Page<TableMenuCartResponse> findAll(String name, int page, int size) {
        Page<TableMenu> tableMenus = tableMenuRepository.findAllByMenuName(
                name, PageRequest.of(page, size));
        return tableMenus.map(tableMenuMapper::toResponse);
    }

    @Override
    public Page<TableMenuResponse> getAll(Long id, int page, int size) {
        Page<TableMenu> tableMenus = tableMenuRepository.findAllByUser(
                id, PageRequest.of(page, size));
        return tableMenus.map(tableMenuMapper::toRepon);

    }

    @Override
    public Page<TableMenuCartResponse> getTableMenu(Long id, int page, int size) {
        Page<TableMenu> tableMenus = tableMenuRepository.findAllByUser(
                id, PageRequest.of(page, size));
        return tableMenus.map(tableMenuMapper::toResponse);
    }

    @Override
    public List<TableMenuCartResponse> getDetails(Long id) {
        List<TableMenu> tableMenus = tableMenuRepository.findAllByReservation(id);
        return tableMenus.stream().map(tableMenuMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Reservation addCart(Long id, Long idUser, Long idTable) {
        Optional<Reservation> existingReservation = reservationRepository.findPendingReservationByUserId(idUser);
        Reservation r;

        if (!existingReservation.isPresent()) {
            Optional<User> user = userRepository.findById(idUser);
            Optional<Tables> tables = tableRepository.findById(idTable);
            r = new Reservation();
            r.setUser(user.orElse(null));
            r.setTable(tables.orElse(null));
            r.setCreatedDate(new Date());
            r.setStatus(Status.PENDING);
            reservationRepository.save(r);
        } else {
            r = existingReservation.get();
        }

        Optional<Menu> menuOptional = menuRepository.findById(id);
        if (menuOptional.isPresent()) {
            Menu menu = menuOptional.get();
            List<TableMenu> tableMenuCartResponses = tableMenuRepository.findAll();
            boolean menuInCart = false;

            for (TableMenu tableMenu : tableMenuCartResponses) {
                if (tableMenu.getMenu().getId().equals(menu.getId()) && tableMenu.getReservation().getId().equals(r.getId())) {
                    tableMenu.setQuantity(tableMenu.getQuantity() + 1);
                    tableMenu.setPrice(menu.getPrice() * tableMenu.getQuantity());
                    menuInCart = true;
                    tableMenuRepository.save(tableMenu);
                    break;
                }
            }

            if (!menuInCart) {
                TableMenuRequest tableMenuRequest = new TableMenuRequest();
                tableMenuRequest.setMenu(menu);
                tableMenuRequest.setReservation(r);
                tableMenuRequest.setPrice(menu.getPrice());
                save(tableMenuRequest);
            }
        }
        return r;
    }


    @Override
    public void removeCartItem(Long id) {
        for (TableMenu tableMenu : tableMenuRepository.findAll()) {
            if (tableMenu.getId().equals(id)) {
                if (tableMenu.getQuantity() <= 1) {
                    tableMenuRepository.deleteById(tableMenu.getId());
                } else {
                    tableMenu.setQuantity(tableMenu.getQuantity() - 1);
                    tableMenuRepository.save(tableMenu);
                }
                break;
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


    @Override
    public void changeStatus(Long id) {
        Optional<TableMenu> tableMenu = tableMenuRepository.findById(id);
        if (tableMenu.isPresent()) {
            tableMenu.get().setStatus(!tableMenu.get().isStatus());
            tableMenuRepository.save(tableMenu.get());
        }
    }

}
