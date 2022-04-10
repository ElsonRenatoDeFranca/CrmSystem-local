package com.addi.challenge.externalsystem.crmsystem.service;

import com.addi.challenge.externalsystem.crmsystem.entity.Person;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonNotProvidedException;
import com.addi.challenge.externalsystem.crmsystem.repository.CrmSystemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrmSystemServiceImplTest {

    @Mock
    private CrmSystemRepository repository;

    @InjectMocks
    private CrmSystemServiceImpl crmSystemService;

    @Test
    public void shouldReturnANotEmptyListWhenFindAllIsCalledAndThereIsAtLeastOneItemInTheDatabase() {
        when(repository.findAll()).thenReturn(createNotEmptyPersonMockList());
        List<Person> actualPerson = this.crmSystemService.findAll();

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.isEmpty()).isFalse();
    }

    @Test
    public void shouldReturnAnEmptyListWhenFindAllIsCalledAndThereIsNoItemInDatabase() {
        when(repository.findAll()).thenReturn(createEmptyPersonMockList());
        List<Person> actualPerson = this.crmSystemService.findAll();

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnNotNullWhenFindByNationalIdentificationNumberIsCalled() {
        Person expectedPerson = createPersonMock();

        when(repository.findByNationalIdentificationNumber(any())).thenReturn(expectedPerson);

        Person actualPerson = this.crmSystemService.findByNationalIdentificationNumber(expectedPerson.getNationalIdentificationNumber());

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson).isEqualTo(actualPerson);
    }

    @Test
    public void shouldAddANewPersonToTheDatabaseWhenSaveIsCalled() throws PersonMismatchException, PersonNotProvidedException {
        Person expectedPerson = createPersonMock();

        when(repository.save(any())).thenReturn(expectedPerson);

        Person actualPerson = this.crmSystemService.save(expectedPerson);

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson).isEqualTo(expectedPerson);
        verify(repository, atLeast(1)).save(any());
    }

    private List<Person> createNotEmptyPersonMockList() {
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