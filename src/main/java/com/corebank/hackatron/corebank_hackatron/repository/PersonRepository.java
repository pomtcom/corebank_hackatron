package com.corebank.hackatron.corebank_hackatron.repository;

import com.corebank.hackatron.corebank_hackatron.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, String> {
}
