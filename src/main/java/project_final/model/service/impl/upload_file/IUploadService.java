package project_final.model.service.impl.upload_file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface IUploadService {
    String uploadFile(MultipartFile file);
    File convertMultiPartFileToFile(MultipartFile file);
}
