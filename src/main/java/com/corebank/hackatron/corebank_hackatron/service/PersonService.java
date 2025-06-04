package com.corebank.hackatron.corebank_hackatron.service;

import com.corebank.hackatron.corebank_hackatron.model.Person;
import com.corebank.hackatron.corebank_hackatron.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void registerPerson(Person person) {
        personRepository.save(person);
    }
}