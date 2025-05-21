package it.uniroma3.siw.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ImageRepository;
import jakarta.transaction.Transactional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    //@Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public Image saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = originalFilename.substring(dotIndex);
        }

        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);

        Image image = new Image();
        image.setName(originalFilename);
        image.setType(file.getContentType());
        image.setFilename(uniqueFilename);

        return imageRepository.save(image);
    }

    @Transactional()
    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id);
    }

    @Transactional
    public void deleteImage(Long imageId) throws IOException {
        Optional<Image> imageOptional = imageRepository.findById(imageId);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            Path filePath = Paths.get(uploadDir).resolve(image.getFilename());
            Files.deleteIfExists(filePath);
            imageRepository.delete(image);
        }
    }
}

