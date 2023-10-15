package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.entity.TableType;
import project_final.repository.ITableTypeRepository;
import project_final.service.IUploadService;



import java.util.Optional;

@Component
@AllArgsConstructor
public class TableTypeMapper implements ITableTypeMapper {
    private final IUploadService uploadService;
    private final ITableTypeRepository tableTypeRepository;
    @Override
    public TableType toEntity(TableTypeRequest tableTypeRequest) {
        // check table
        Optional<TableType> table = tableTypeRequest.getId() != null ?
                tableTypeRepository.findById(tableTypeRequest.getId()) :
                Optional.empty();

        String image;
        if (tableTypeRequest.getImage() != null && !tableTypeRequest.getImage().isEmpty()) {
            // nếu có ảnh mới
            image = uploadService.uploadFile(tableTypeRequest.getImage());
        } else if (table.isPresent()) {
            // nếu category  tồn tại
            image = table.get().getImage();
        } else {
            // không có ảnh và không tồn tại category
            image = "../../assets/images/avatars/01.png";
        }

            return TableType.builder()
                    .id(tableTypeRequest.getId())
                    .name(tableTypeRequest.getName())
                    .image(image)
                    .description(tableTypeRequest.getDescription())
                    .build();

    }

    @Override
    public TableTypeResponse toResponse(TableType tableType) {
        return TableTypeResponse.builder()
                .id(tableType.getId())
                .name(tableType.getName())
                .image(tableType.getImage())
                .description(tableType.getDescription())
                .status(tableType.isStatus()).build();
    }
}
