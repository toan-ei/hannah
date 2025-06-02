package com.hannah.file_service.service;

import com.hannah.file_service.dto.response.FileDataResponse;
import com.hannah.file_service.dto.response.FileResponse;
import com.hannah.file_service.entity.FileManagement;
import com.hannah.file_service.mapper.FileMapper;
import com.hannah.file_service.repository.FileManagementRepository;
import com.hannah.file_service.repository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {
    FileRepository fileRepository;
    FileMapper fileMapper;
    FileManagementRepository fileManagementRepository;

    public FileResponse uploadFiles(MultipartFile file) throws IOException {
        FileDataResponse fileDataResponse = fileRepository.storage(file);

        FileManagement fileManagement = fileMapper.toFileManagement(fileDataResponse);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        fileManagement.setOwnerId(authentication.getName());
        fileManagementRepository.save(fileManagement);

        return FileResponse.builder()
                .url(fileDataResponse.getUrl())
                .build();
    }
}
