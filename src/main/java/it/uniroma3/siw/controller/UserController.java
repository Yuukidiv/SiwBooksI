package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class UserController {
	
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	UserService userService;
	
	
	// delete user -> cancellare user e cancellare credentials I guess. Cancellandoli le liste delle loro recensioni verranno cancellate pure loro 
	// se volessi che restassero allora dovrei collegarle ad un controller penso.
	
	/* per ora il metodo lo tolgo stica
	 * @PostMapping("/delete/profile") public String deleteUser(Model model,
	 * HttpServletRequest request) {
	 * 
	 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 * UserDetails userDetails = (UserDetails) auth.getPrincipal();
	 * 
	 * 
	 * Credentials credentials =
	 * credentialsService.getCredentials(userDetails.getUsername()); User user =
	 * credentials.getUser();
	 * 
	 * SecurityContextHolder.clearContext(); request.getSession().invalidate();
	 * 
	 * this.credentialsService.delete(credentials); this.userService.delete(user);
	 * 
	 * return "redirect:/"; }
	 */

}
