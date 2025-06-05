package it.uniroma3.siw.controller;

import org.springframework.http.HttpHeaders;  
import org.springframework.http.MediaType;       

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.service.PhotoService;

@Controller
public class PhotoController {
	 @Autowired
	    private PhotoService photoService;

	    @GetMapping("/photo/{id}")
	    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
	        Optional<Photo> optionalPhoto = photoService.getPhoto(id);
	        if (optionalPhoto.isPresent()) {
	            Photo photo = optionalPhoto.get();
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.parseMediaType(photo.getContentType()));
	            return new ResponseEntity<>(photo.getImageData(), headers, HttpStatus.OK);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
}
