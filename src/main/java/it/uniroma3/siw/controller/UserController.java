package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.UserValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PhotoService;
import it.uniroma3.siw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Controller
public class UserController {
	
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@Autowired
	private UserValidator userValidator;
	
	
	@GetMapping("/profile") 
	public String getProfile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();

	    
	    Credentials credenziali = credentialsService.getCredentials(userDetails.getUsername());
	    User utente = credenziali.getUser();
	    
	    model.addAttribute("user", utente);
	    model.addAttribute("reviews", utente.getWrittenReviews());
	    model.addAttribute("credentials", credenziali);

		return "profile.html";
	}
	
	// modifica profilo 
	@GetMapping("/profile/editProfile")
	public String editProfile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();

	    
	    Credentials credenziali = credentialsService.getCredentials(userDetails.getUsername());
	    User utente = credenziali.getUser();
		model.addAttribute("credentials", credenziali);
		model.addAttribute("user", utente);
		
		return "editProfile.html";
	}
	
	@Transactional
	@PostMapping("/profile/editProfile")
	public String saveEditProfile(@ModelAttribute("user") User user, BindingResult userBindingResult,
	                              @ModelAttribute("credentials") Credentials credentials,
	                              @RequestParam(name = "file", required = false) MultipartFile photoFile,
	                              Model model) throws IOException {

	    User existingUser = userService.getUser(user.getId());
	    
	    userValidator.validate(user, userBindingResult);
	    
	    if (userBindingResult.hasErrors()) {
	    	return "editProfile";
	    }

	    existingUser.setName(user.getName());
	    existingUser.setSurname(user.getSurname());
	    existingUser.setEmail(user.getEmail());

	    if (photoFile != null && !photoFile.isEmpty()) {
	        Photo photo = existingUser.getPhoto();
	        if (photo == null) {
	            photo = new Photo();
	            photo.setUser(existingUser);
	        }
	        photo.setData(photoFile.getBytes());
	        existingUser.setPhoto(photo);
	    }

	    userService.saveUser(existingUser);

	    model.addAttribute("success", "Perfect, you have modified your profile!");
	    return "redirect:/profile";
	}
	
	
	


	
	@GetMapping("/profile/modifyPassword") 
	public String editPsw(Model model) {
		return "editPsw.html";
	}
	
	@PostMapping("/profile/modifyPassword") 
	public String modPsw(Model model, @RequestParam("oldPsw") String oldPsw, @RequestParam("newPsw") String newPsw,
							@RequestParam("confirmPsw") String confirmPsw) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();

	    
	    Credentials credenziali = credentialsService.getCredentials(userDetails.getUsername());
	    User user = credenziali.getUser();
	    
	    if (!passwordEncoder.matches(oldPsw, credenziali.getPassword())) {
	    	model.addAttribute("msgError", "Wrong Password");
	    	return "editPsw.html";
	    }
	    
	    if(newPsw.length()<5) {
	    	model.addAttribute("msgError", "La Password deve avere una lunghezza minima di 5.");
	    	return "editPsw";
	    }
	    
	    if (oldPsw.equals(newPsw)) {
	    	model.addAttribute("msgError", "La nuova password deve essere diversa da quella vecchia.");
	    	return "editPsw";
	    }
	    
	    if (!newPsw.equals(confirmPsw))  {
	    	model.addAttribute("msgError", "Le password non sono uguali.");
	
	    	return "editPsw.html";
	    }
	    
	    // se tutto va bene
	    credenziali.setPassword(newPsw);
	    credentialsService.saveCredentials(credenziali);
	    
	    model.addAttribute("success", "modifiche apportate!");
	    
		return "redirect:/profile";
	}
	
}
