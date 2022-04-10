package com.addi.challenge.externalsystem.crmsystem.service;

import com.addi.challenge.externalsystem.crmsystem.entity.Person;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonNotFoundException;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonNotProvidedException;

import java.util.List;

public interface CrmSystemService {
    List<Person> findAll();

    Person findByNationalIdentificationNumber(String nationalIdentificationNumber);

    Person save(Person person) throws PersonMismatchException, PersonNotProvidedException;

    void deleteByNationalIdentificationNumber(String nationalIdentificationNumber) throws PersonNotFoundException;
}
