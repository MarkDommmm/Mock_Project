package project_final.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface IUploadService {
    String uploadFile(MultipartFile file);
    File convertMultiPartFileToFile(MultipartFile file);
}
