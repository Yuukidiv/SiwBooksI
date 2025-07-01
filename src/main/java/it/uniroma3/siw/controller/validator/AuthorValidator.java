package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;

@Component
public class AuthorValidator implements Validator {

    @Autowired
    private AuthorService authorService;

    @Override 
    public void validate(Object o, Errors errors) {
        Author author = (Author) o;
        LocalDate today = LocalDate.now();
        
        if (author.getName() != null)
            author.setName(author.getName().trim());

        if (author.getSurname() != null)
            author.setSurname(author.getSurname().trim());
        
        boolean exists = authorService.existsByNameAndSurnameAndDateOfBirth(
            author.getName(), author.getSurname(), author.getDateOfBirth()
        );


        if (author.getName() != null && author.getSurname() != null && author.getDateOfBirth() != null && exists) {
            errors.reject("author.duplicate");
        }
        if (author.getDateOfBirth() != null) {
            if (author.getDateOfBirth().isAfter(today)) {
                errors.rejectValue("dateOfBirth", "author.birth.future", "La data di nascita non può essere nel futuro.");
            }
        }

        if (author.getDateOfDeath() != null) {
            if (author.getDateOfDeath().isAfter(today)) {
                errors.rejectValue("dateOfDeath", "author.death.future", "La data di morte non può essere nel futuro.");
            }

            if (author.getDateOfBirth() != null && author.getDateOfDeath().isBefore(author.getDateOfBirth())) {
                errors.rejectValue("deathDate", "author.death.beforeBirth", "La data di morte non può essere prima della nascita.");
            }
        }
        
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Author.class.equals(aClass);
    }
}

