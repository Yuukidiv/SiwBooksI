package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.repository.PhotoRepository;

@Service
public class PhotoService {

	@Autowired
    private PhotoRepository photoRepository;

	// provo per ora con gli autori magari
    public void savePhoto(Photo photo) {
        photoRepository.save(photo);
    }

    public Photo findById(Long id) {
		return this.photoRepository.findById(id).orElse(null);
	}
	
	public List<Photo> findAll(){
		return (List<Photo>) this.photoRepository.findAll();
	}

	public void deletePhoto(Photo oldPhoto) {
		this.photoRepository.delete(oldPhoto);
		
	}
    
}
