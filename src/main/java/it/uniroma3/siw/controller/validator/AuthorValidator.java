package it.uniroma3.siw.controller.validator;

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

        System.out.println(">>> VALIDATOR ATTIVO");
        System.out.println(">>> Autore da validare: " + author.getName() + " " + author.getSurname() + " " + author.getDateOfBirth());

        boolean exists = authorService.existsByNameAndSurnameAndDateOfBirth(
            author.getName(), author.getSurname(), author.getDateOfBirth()
        );

        System.out.println(">>> Il metodo existsByNameAndSurnameAndDateOfBirth ha restituito: " + exists);

        if (author.getName() != null && author.getSurname() != null && author.getDateOfBirth() != null && exists) {
            System.out.println(">>> ERRORE: autore già presente");
            errors.reject("author.duplicate");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Author.class.equals(aClass);
    }
}

