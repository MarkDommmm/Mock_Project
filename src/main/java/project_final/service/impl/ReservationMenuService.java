package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project_final.entity.*;
import project_final.model.domain.Status;
import project_final.model.dto.request.ReservationMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.ReservationMenuResponse;
import project_final.repository.*;
import project_final.service.IReservationMenuService;
import project_final.service.mapper.IReservationMenuMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationMenuService implements IReservationMenuService {
    private final IReservationMenuRepository reservationMenuRepository;
    private final IReservationMenuMapper reservationMenuMapper;
    private final IMenuRepository menuRepository;
    private final IReservationRepository reservationRepository;

    @Override
    public Page<TableMenuCartResponse> findAll(String name, int page, int size) {
        Page<ReservationMenu> tableMenus = reservationMenuRepository.findAllByMenuName(
                name, PageRequest.of(page, size));
        return tableMenus.map(reservationMenuMapper::toResponse);
    }

    @Override
    public Page<TableMenuCartResponse> getAll(Long id, int page, int size) {
        Page<ReservationMenu> tableMenus = reservationMenuRepository.findAllByReservationId(
                id, PageRequest.of(page, size));
        return tableMenus.map(reservationMenuMapper::toResponse);

    }

    @Override
    public Page<TableMenuCartResponse> getTableMenu(Long id, int page, int size) {
        Page<ReservationMenu> tableMenus = reservationMenuRepository.findAllByReservationId(
                id, PageRequest.of(page, size));
        return tableMenus.map(reservationMenuMapper::toResponse);
    }

    @Override
    public Page<ReservationMenuResponse> getReservationMenu(Long id, int page, int size) {
        Reservation reservation = reservationRepository.findById(id).get();
        Page<ReservationMenu> reservationMenus = reservationMenuRepository.findByReservation(reservation, PageRequest.of(page, size));
        return reservationMenus.map(reservationMenuMapper::toReponse);
    }

    @Override
    public List<TableMenuCartResponse> getDetails(Long id) {
        List<ReservationMenu> reservationMenus = reservationMenuRepository.findAllByReservationId(id);
        return reservationMenus.stream().map(reservationMenuMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public ReservationMenu served(int quantity, Long id) {
        Optional<ReservationMenu> reservationMenus = reservationMenuRepository.findById(id);
        if (reservationMenus.isPresent()) {
            reservationMenus.get().setQuantityDelivered(quantity);
            return reservationMenuRepository.save(reservationMenus.get());
        }
        return null;
    }


    @Override
    public void addCart(Long id, Long idR) {

        Optional<Reservation> existingReservation = reservationRepository.findPendingReservationByReservationId(idR);

        Optional<Menu> menuOptional = menuRepository.findById(id);
        if (menuOptional.isPresent()) {
            Menu menu = menuOptional.get();
            List<ReservationMenu> reservationMenuCartRespons = reservationMenuRepository.findAll();
            boolean menuInCart = false;

            for (ReservationMenu reservationMenu : reservationMenuCartRespons) {
                if (reservationMenu.getMenu().getId().equals(menu.getId())
                        && reservationMenu.getReservation().getId().equals(existingReservation.get().getId())
                        && reservationMenu.getPay().equals(Status.UN_PAID)) {
//                    double price = menu.getPrice() * reservationMenu.getQuantityOrdered();
//                    double roundedResults = (double) Math.round(price * 10.0) / 10;
                    reservationMenu.setQuantityOrdered(reservationMenu.getQuantityOrdered() + 1);
                    reservationMenu.setPrice(menu.getPrice() * reservationMenu.getQuantityOrdered());
                    menuInCart = true;
                    reservationMenuRepository.save(reservationMenu);
                    break;
                }
            }

            if (!menuInCart) {
                ReservationMenuRequest tableMenuRequest = new ReservationMenuRequest();
                tableMenuRequest.setMenu(menu);
                tableMenuRequest.setReservation(existingReservation.get());
                tableMenuRequest.setPrice(menu.getPrice());
                save(tableMenuRequest);
            }
        }
    }


    @Override
    public void removeCartItem(Long id) {
        for (ReservationMenu reservationMenu : reservationMenuRepository.findAll()) {
            if (reservationMenu.getId().equals(id)) {
                if (reservationMenu.getQuantityOrdered() <= 1) {
                    reservationMenuRepository.deleteById(reservationMenu.getId());
                } else {
                    reservationMenu.setQuantityOrdered(reservationMenu.getQuantityOrdered() - 1);
                    reservationMenu.setPrice(reservationMenu.getPrice() - reservationMenu.getMenu().getPrice());
                    reservationMenuRepository.save(reservationMenu);
                }
                break;
            }
        }
    }


    @Override
    public TableMenuCartResponse findById(Long id) {
        Optional<ReservationMenu> tableMenu = reservationMenuRepository.findById(id);
        if (tableMenu.isPresent()) {
            return reservationMenuMapper.toResponse(tableMenu.get());
        }
        return null;
    }

    @Override
    public void save(ReservationMenuRequest tableMenuRequest) {
        reservationMenuRepository.save(reservationMenuMapper.toEntity(tableMenuRequest));
    }

    @Override
    public void delete(Long id) {
        Optional<ReservationMenu> tableMenu = reservationMenuRepository.findById(id);
        if (tableMenu.isPresent()) {
            reservationMenuRepository.deleteById(id);
        }
    }


    @Override
    public String adminCancel(Long id) {
        Optional<ReservationMenu> reservationMenu = reservationMenuRepository.findById(id);
        if (reservationMenu.isPresent()) {
            if (reservationMenu.get().getPay().equals(Status.UN_PAID) &&
                    reservationMenu.get().getQuantityDelivered() == 0) {
                reservationMenuRepository.deleteById(id);
                return "Your file has been deleted.";
            } else {
                return "This menu cannot be deleted";
            }
        }
        return "Menu is not found";
    }
}
