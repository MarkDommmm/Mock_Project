package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.Menu;
import project_final.model.dto.request.TableTypeRequest;
import project_final.model.dto.response.TableTypeResponse;
import project_final.entity.TableType;
import project_final.repository.ITableTypeRepository;
import project_final.service.IUploadService;
import project_final.service.mapper.ITableTypeMapper;

import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TableTypeMapper implements ITableTypeMapper {
    private final IUploadService uploadService;
    private final ITableTypeRepository tableTypeRepository;
    @Override
    public TableType toEntity(TableTypeRequest tableTypeRequest) {
        MultipartFile oldImage = tableTypeRequest.getImage();
        if (oldImage.isEmpty()) {
            Optional<TableType> tableType = tableTypeRepository.findById(tableTypeRequest.getId());
            return TableType.builder()
                    .id(tableTypeRequest.getId())
                    .name(tableTypeRequest.getName())
                    .image(tableType.get().getImage())
                    .description(tableTypeRequest.getDescription())
                    .status(true).build();
        } else {
            String image = uploadService.uploadFile(tableTypeRequest.getImage());
            return TableType.builder()
                    .id(tableTypeRequest.getId())
                    .name(tableTypeRequest.getName())
                    .image(image)
                    .description(tableTypeRequest.getDescription())
                    .status(true).build();
        }
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
