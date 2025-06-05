package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.repository.PhotoRepository;

@Service
public class PhotoService {

	@Autowired
    private PhotoRepository photoRepository;

    public void savePhoto(Photo photo) {
        photoRepository.save(photo);
    }

    public Optional<Photo> getPhoto(Long id) {
        return photoRepository.findById(id);
    }
    
    
}
