package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.service.BookService;

@Component
public class BookValidator implements Validator{

	@Autowired
	private BookService bookService;
	
	@Override 
	public void validate(Object o, Errors errors) {
		Book book = (Book)o;
		
		if (book.getTitle() != null)
		    book.setTitle(book.getTitle().trim());
		
		if (book.getTitle()!=null && book.getDateOfPublication()!=null
				&& bookService.existsByTitleAndDateOfPublication(book.getTitle(), book.getDateOfPublication())) {
			errors.reject("book.duplicate");
		}
		
		if (book.getDateOfPublication() != null) {
            int currentYear = java.time.Year.now().getValue();
            if (book.getDateOfPublication() > currentYear) {
                errors.rejectValue("dateOfPublication", "book.date.future", "La data di pubblicazione non può essere nel futuro.");
            }
            if (book.getDateOfPublication() < 1400) {
                errors.rejectValue("dateOfPublication", "book.date.tooOld", "La data di pubblicazione è troppo antica.");
            }
        }
		
		
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Book.class.equals(aClass);
	}
}
