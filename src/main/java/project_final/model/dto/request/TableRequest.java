package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.TableType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableRequest {
    private int tableNumber;
    private MultipartFile tableImage;
    private TableType tableType;
    private String description;

}
