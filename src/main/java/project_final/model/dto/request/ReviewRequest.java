package project_final.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project_final.entity.Reservation;
import project_final.entity.User;


import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {
    private Long id;
    private MultipartFile image;
    private String comment;
    private int rating;
    private Date createdDate;
    private Reservation reservation;
}
