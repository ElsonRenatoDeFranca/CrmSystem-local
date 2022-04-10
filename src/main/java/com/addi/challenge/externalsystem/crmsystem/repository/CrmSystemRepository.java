package com.addi.challenge.externalsystem.crmsystem.repository;

import com.addi.challenge.externalsystem.crmsystem.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmSystemRepository extends JpaRepository<Person, Long> {
    Person findByNationalIdentificationNumber(String nationalIdentificationNumber);
    void deleteByNationalIdentificationNumber(String nationalIdentificationNumber);
}
