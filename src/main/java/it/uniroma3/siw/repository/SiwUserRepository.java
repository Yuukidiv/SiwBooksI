package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.SiwUser;

public interface SiwUserRepository extends CrudRepository <SiwUser, Long>{

}
