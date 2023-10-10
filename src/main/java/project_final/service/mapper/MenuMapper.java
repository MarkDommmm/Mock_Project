package project_final.service.mapper;

;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.model.dto.request.MenuRequest;
import project_final.model.dto.response.MenuResponse;
import project_final.entity.Menu;
import project_final.service.IUploadService;
import project_final.service.mapper.IMenuMapper;

import java.util.Date;
@Component
@AllArgsConstructor
public class MenuMapper implements IMenuMapper {
    private final IUploadService uploadService;
    @Override
    public MenuResponse toResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .image(menu.getImage())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .creatDate(menu.getCreateDate())
                .category(menu.getCategory())
                .status(menu.isStatus()).build();
    }

    @Override
    public Menu toEntity(MenuRequest menuRequest) {
        String url = uploadService.uploadFile(menuRequest.getImage());
        return Menu.builder()
                .id(menuRequest.getId())
                .name(menuRequest.getName())
                .image(url)
                .description(menuRequest.getDescription())
                .price(menuRequest.getPrice())
                .createDate(new Date())
                .status(true)
                .category(menuRequest.getCategory()).build();
    }
}