package project_final.service.mapper;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project_final.model.dto.request.MenuRequest;
import project_final.model.dto.response.MenuResponse;
import project_final.entity.Menu;
import project_final.repository.IMenuRepository;
import project_final.service.IUploadService;

import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class MenuMapper implements IMenuMapper {
    private final IUploadService uploadService;
    private final IMenuRepository menuRepository;

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
        // check Menu
        Optional<Menu> existingMenu= menuRequest.getId() != null ?
                menuRepository.findById(menuRequest.getId()) :
                Optional.empty();

        String image;
        if (menuRequest.getImage() != null && !menuRequest.getImage().isEmpty()) {
            // nếu có ảnh mới
            image = uploadService.uploadFile(menuRequest.getImage());
        } else if (existingMenu.isPresent()) {
            // nếu menu  tồn tại
            image = existingMenu.get().getImage();

        } else {
            // Không có ảnh và không tồn tại category
            image = "../../assets/images/avatars/01.png";
        }
            return Menu.builder()
                    .id(menuRequest.getId())
                    .name(menuRequest.getName())
                    .image(image)
                    .description(menuRequest.getDescription())
                    .price(menuRequest.getPrice())
                    .createDate(new Date())
                    .status(true)
                    .category(menuRequest.getCategory()).build();
        }
}
