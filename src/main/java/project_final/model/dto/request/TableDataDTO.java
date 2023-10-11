package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Page;
import project_final.model.dto.response.TableResponse;
import project_final.model.dto.response.TableTypeResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableDataDTO {
    private Page<TableTypeResponse> tableTypes;
    private List<TableResponse> tables;
}
