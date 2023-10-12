package project_final.service.mapper;

import org.springframework.stereotype.Component;
import project_final.entity.TableMenu;
import project_final.model.dto.request.TableMenuRequest;
import project_final.model.dto.response.TableMenuCartResponse;
import project_final.model.dto.response.TableMenuResponse;;
@Component
public class TableMenuMapper implements ITableMenuMapper{
    @Override
    public TableMenu toEntity(TableMenuRequest tableMenuRequest) {
        return TableMenu.builder()
                .id(tableMenuRequest.getId())
                .menu(tableMenuRequest.getMenu())
                .reservation(tableMenuRequest.getReservation())
                .quantity(tableMenuRequest.getQuantity())
                .price(tableMenuRequest.getMenu().getPrice())
                .status(true).build();
    }

    @Override
    public TableMenuCartResponse toResponse(TableMenu tableMenu) {
        return TableMenuCartResponse.builder()
                .id(tableMenu.getId())
                .image(tableMenu.getMenu().getImage())
                .name(tableMenu.getMenu().getName())
                .quantity(tableMenu.getQuantity())
                .price(tableMenu.getPrice())
                .status(tableMenu.isStatus()).build();
    }

    @Override
    public TableMenuResponse toRepon(TableMenu tableMenu) {
        return TableMenuResponse.builder()
                .id(tableMenu.getId())
                .menu(tableMenu.getMenu())
                .reservation(tableMenu.getReservation())
                .quantity(tableMenu.getQuantity())
                .price(tableMenu.getPrice())
                .status(tableMenu.isStatus()).build();
    }
}
