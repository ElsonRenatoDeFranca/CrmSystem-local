package com.addi.challenge.externalsystem.crmsystem.controller;

import com.addi.challenge.externalsystem.crmsystem.entity.Person;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonNotFoundException;
import com.addi.challenge.externalsystem.crmsystem.exception.PersonNotProvidedException;
import com.addi.challenge.externalsystem.crmsystem.service.CrmSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crm")
public class CrmSystemController {

    private final CrmSystemService crmSystemService;

    public CrmSystemController(CrmSystemService judicialNationalArchivesSystemService) {
        this.crmSystemService = judicialNationalArchivesSystemService;
    }

    @GetMapping
    @Operation(summary = "This operation is to fetch all people stored in CRM")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched all the people from CRM",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "Service is not available",
                    content = @Content)

    })
    public ResponseEntity<List<Person>> findAll() {
        return new ResponseEntity<>(crmSystemService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "This operation is to save a specific person to CRM")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Saved a person to CRM",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The person was not found at CRM",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "406",
                    description = "The correct information was not sent to database",
                    content = {@Content(mediaType = "application/json")}),

            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    public ResponseEntity<Person> save(@RequestBody Person person) {
        try {
            Person savedPerson = crmSystemService.save(person);
            return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
        } catch (PersonMismatchException | PersonNotProvidedException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{nationalIdentificationNumber}")
    @ResponseBody
    @Operation(summary = "This operation is to fetch a specific person stored in CRM by using its national ID number as key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched a person from CRM",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The person was not found at CRM",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    public ResponseEntity<Person> findByNationalIdentificationNumber(@PathVariable("nationalIdentificationNumber") String nationalIdentificationNumber) {
        Person person = crmSystemService.findByNationalIdentificationNumber(nationalIdentificationNumber);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @DeleteMapping("/{nationalIdentificationNumber}")
    @Operation(summary = "This operation is to delete a specific person stored in CRM by using its national ID number as key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched a person from CRM",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The person was not found at CRM",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    void deleteByNationalIdentificationNumber(@PathVariable String nationalIdentificationNumber) {
        try {
            crmSystemService.deleteByNationalIdentificationNumber(nationalIdentificationNumber);
        } catch (PersonNotFoundException e) {
            new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
