package com.ecommerce.project;

import com.ecommerce.project.service.FileServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplementationTest {

    @InjectMocks
    private FileServiceImplementation fileService;

    @TempDir
    Path tempDir;

    @Test
    void uploadImage_shouldSaveFileToTempDirectory() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "test image content".getBytes()
        );

        String uploadPath = tempDir.toString();

        String savedFileName = fileService.uploadImage(uploadPath, mockFile);

        assertNotNull(savedFileName);
        assertTrue(savedFileName.endsWith(".png"));

        Path savedFilePath = tempDir.resolve(savedFileName);
        assertTrue(Files.exists(savedFilePath));

        String fileContent = Files.readString(savedFilePath);
        assertEquals("test image content", fileContent);
    }
}
