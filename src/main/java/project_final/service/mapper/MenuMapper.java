package project_final.service.mapper;

;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.Tables;
import project_final.model.dto.request.MenuRequest;
import project_final.model.dto.response.MenuResponse;
import project_final.entity.Menu;
import project_final.repository.IMenuRepository;
import project_final.service.IUploadService;
import project_final.service.mapper.IMenuMapper;

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
        MultipartFile oldImage = menuRequest.getImage();
        if (oldImage.isEmpty()) {
            Optional<Menu> menu = menuRepository.findById(menuRequest.getId());
            return Menu.builder()
                    .id(menuRequest.getId())
                    .name(menuRequest.getName())
                    .image(menu.get().getImage())
                    .description(menuRequest.getDescription())
                    .price(menuRequest.getPrice())
                    .createDate(new Date())
                    .status(true)
                    .category(menuRequest.getCategory()).build();
        } else {
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
}
