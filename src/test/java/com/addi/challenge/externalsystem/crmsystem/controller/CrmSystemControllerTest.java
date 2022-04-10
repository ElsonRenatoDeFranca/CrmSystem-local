package com.addi.challenge.externalsystem.crmsystem.controller;

import com.addi.challenge.externalsystem.crmsystem.entity.Person;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonNotFoundException;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonNotProvidedException;
import com.addi.challenge.externalsystem.crmsystem.service.CrmSystemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CrmSystemControllerTest {

    private static final String PERSON_NOT_FOUND_EXCEPTION_MESSAGE = "Person not found in CRM";
    private static final String PERSON_ALREADY_EXISTS_IN_CRM_MESSAGE = "Person already exists in CRM";
    private static final String PERSON_NOT_PROVIDED_EXCEPTION_MESSAGE = "Person not provided to be saved.";

    @Mock
    private CrmSystemService crmSystemService;

    @InjectMocks
    private CrmSystemController crmSystemController;

    @Test
    public void shouldReturnANotEmptyListWhenFindAllIsCalledAndThereIsAtLeastOneItemInTheDatabase() {
        when(crmSystemService.findAll()).thenReturn(createNotEmptyPeopleMockList());

        ResponseEntity<List<Person>> actualPeople = this.crmSystemController.findAll();

        assertThat(actualPeople).isNotNull();
        assertThat(actualPeople.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnAnEmptyListWhenFindAllIsCalledAndThereIsNoItemInDatabase() {
        when(crmSystemService.findAll()).thenReturn(createEmptyPersonMockList());

        ResponseEntity<List<Person>> actualPeople = this.crmSystemController.findAll();

        assertThat(actualPeople).isNotNull();
        assertThat(actualPeople.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnNotNullWhenFindByIdIsCalled() {
        Person expectedPerson = createPersonMock();

        when(crmSystemService.findByNationalIdentificationNumber(any())).thenReturn(expectedPerson);

        ResponseEntity<Person> actualPerson = this.crmSystemController.findByNationalIdentificationNumber(expectedPerson.getNationalIdentificationNumber());

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualPerson.getBody()).isEqualTo(expectedPerson);
    }

    @Test
    public void shouldDeleteAnExistingPersonFromTheDatabaseWhenDeletePersonIsCalled() throws PersonNotFoundException {
        Person expectedPerson = createPersonMock();

        doNothing().when(crmSystemService).deleteByNationalIdentificationNumber(any());

        this.crmSystemController.deleteByNationalIdentificationNumber(expectedPerson.getNationalIdentificationNumber());

        verify(crmSystemService, atLeast(1)).deleteByNationalIdentificationNumber(any());
    }

    @Test
    public void shouldAddANewPersonToTheDatabaseWhenAddPersonIsCalled() throws PersonNotProvidedException, PersonMismatchException {
        Person expectedPerson = createPersonMock();

        when(crmSystemService.save(any())).thenReturn(expectedPerson);

        ResponseEntity<Person> actualPerson = this.crmSystemController.save(expectedPerson);

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(crmSystemService, atLeast(1)).save(any());
    }

    @Test
    public void shouldThrowPersonMismatchExceptionWhenTriedToSaveAnAlreadyExistingPerson() throws PersonNotProvidedException, PersonMismatchException {
        Person expectedPerson = createPersonMock();

        when(crmSystemService.save(any())).thenThrow(new PersonMismatchException(PERSON_ALREADY_EXISTS_IN_CRM_MESSAGE));

        Throwable exception = assertThrows(PersonMismatchException.class,
                () -> this.crmSystemService.save(expectedPerson));

        assertThat(PERSON_ALREADY_EXISTS_IN_CRM_MESSAGE).isEqualTo(exception.getMessage());
    }

    @Test
    public void shouldThrowPersonNotProvidedExceptionWhenTriedToSaveWithoutPassingAnyElementInTheBody() throws PersonNotProvidedException, PersonMismatchException {
        Person expectedPerson = createPersonMock();

        when(crmSystemService.save(any())).thenThrow(new PersonNotProvidedException(PERSON_NOT_PROVIDED_EXCEPTION_MESSAGE));

        Throwable exception = assertThrows(PersonNotProvidedException.class,
                () -> this.crmSystemService.save(expectedPerson));

        assertThat(PERSON_NOT_PROVIDED_EXCEPTION_MESSAGE).isEqualTo(exception.getMessage());
    }

    @Test
    public void shouldThrowPersonNotFoundExceptionWhenTriedToDeleteAPersonThatDoesNotExists() throws PersonNotFoundException {
        Person expectedPerson = createPersonMock();

        doThrow(new PersonNotFoundException(PERSON_NOT_FOUND_EXCEPTION_MESSAGE)).when(crmSystemService).deleteByNationalIdentificationNumber(any());

        Throwable exception = assertThrows(PersonNotFoundException.class,
                () -> this.crmSystemService.deleteByNationalIdentificationNumber(expectedPerson.getNationalIdentificationNumber()));

        assertThat(PERSON_NOT_FOUND_EXCEPTION_MESSAGE).isEqualTo(exception.getMessage());

    }


    private List<Person> createNotEmptyPeopleMockList() {
        return Arrays.asList(createPersonMock(), createPersonMock());
    }

    private List<Person> createEmptyPersonMockList() {
        return Collections.emptyList();
    }

    private Person createPersonMock() {
        return Person.builder()
                .birthDate("22/10/2001")
                .email("john@gmail.com")
                .firstName("john")
                .lastName("Mccain")
                .nationalIdentificationNumber("1")
                .id(1L)
                .build();
    }
}