package it.uniroma3.siw.controller.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credentials;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;


@Component
public class CredentialsValidator implements Validator {
	
	@Autowired
	private CredentialsService credentialsService;
	

	 @Override
	    public void validate(Object o, Errors errors) {
	        Credentials credentials = (Credentials) o;

	        if (credentials.getPassword().length() < 5) {
	            errors.rejectValue("password", "credentials.password.invalid", "Le password devono essere di lunghezza minima 5");
	        }

	        if (!credentials.getUsername().matches("^[a-zA-Z0-9._-]{3,}$")) {
	            errors.rejectValue("username", "credentials.username.invalid", "Lo username deve avere almeno 3 caratteri");
	        }
	        
	        if(credentialsService.getCredentials(credentials.getUsername()) != null) {
				errors.rejectValue("username", "credentials.username.invalid","Username già usato. Scegli un nuovo Username");
			}

	        
	    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }
}
