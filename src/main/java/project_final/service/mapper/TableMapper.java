package project_final.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.Category;
import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;
import project_final.entity.Tables;
import project_final.repository.ITableRepository;
import project_final.service.IUploadService;
import project_final.service.mapper.ITableMapper;

import java.util.Optional;

@Component
@AllArgsConstructor
public class TableMapper implements ITableMapper {
    private final IUploadService uploadService;
    private final ITableRepository tableRepository;

    @Override
    public Tables toEntity(TableRequest tableRequest) {
        // check table
        Optional<Tables> table = tableRequest.getId() != null ?
                tableRepository.findById(tableRequest.getId()) :
                Optional.empty();

        String image;
        if (tableRequest.getTableImage() != null && !tableRequest.getTableImage().isEmpty()) {
            // nếu có ảnh mới
            image = uploadService.uploadFile(tableRequest.getTableImage());
        } else if (table.isPresent()) {
            // nếu table tồn tại
            image = table.get().getTableImage();
        } else {
            // không có ảnh và không tồn tại category
            image = "../../assets/images/avatars/01.png";
        }
            return Tables.builder()
                    .id(tableRequest.getId())
                    .name(tableRequest.getName())
                    .tableType(tableRequest.getTableType())
                    .tableImage(image)
                    .description(tableRequest.getDescription())
                    .status(true).build();

    }

    @Override
    public TableResponse toResponse(Tables tables) {
        return TableResponse.builder()
                .id(tables.getId())
                .name(tables.getName())
                .tableImage(tables.getTableImage())
                .tableType(tables.getTableType())
                .description(tables.getDescription())
                .status(tables.isStatus()).build();
    }
}
