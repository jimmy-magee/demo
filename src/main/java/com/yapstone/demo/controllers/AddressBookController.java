package com.yapstone.demo.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yapstone.demo.repositories.AddressBookRepository;
import com.yapstone.demo.domain.AddressBook;
import com.yapstone.demo.dto.AddressBookForm;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
*  Rest Controller for AddressBook api.
*
*/
@RestController
public class AddressBookController {

	private static Logger logger = LoggerFactory.getLogger(AddressBookController.class);
	
	@Autowired
    private AddressBookRepository addressBookRepository;

    @GetMapping("/api/v1/addressbooks")
    public Flux<AddressBook> getAllAddressBookEntries() {
    	logger.debug("Looking up all address book entries");
        return addressBookRepository.findAll();
    }

    @PostMapping("/api/v1/addressbooks")
    public Mono<AddressBook> createAddressBookEntry(@Valid @RequestBody AddressBookForm form) {
    	logger.debug("Creating new address book entry");
    	AddressBook newEntry = AddressBook.builder()
    	.firstName(form.getFirstName())
    	.lastName(form.getLastName())
    	.phoneNumber(form.getPhoneNumber())
    	.address(form.getAddress())
    	.emailAddress(form.getEmailAddress())
    	.build();
        return addressBookRepository.save(newEntry);
    }

    @GetMapping("/api/v1/addressbooks/{id}")
    public Mono<ResponseEntity<AddressBook>> getAddressBookEntryById(@PathVariable(value = "id") String addressBookId) {
    	logger.debug("Get address book entry by id [{}]", addressBookId);
        return addressBookRepository.findById(addressBookId)
                .map(savedAddressBook -> ResponseEntity.ok(savedAddressBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/v1/addressbooks/{id}")
    public Mono<ResponseEntity<AddressBook>> updateAddressBookEntry(@PathVariable(value = "id") String addressBookId,
                                                   @Valid @RequestBody AddressBookForm addressBookForm) {
    	logger.debug("Update address book entry by id [{}]", addressBookId);
        return addressBookRepository.findById(addressBookId)
                .flatMap(existingAddressBook -> {
                    existingAddressBook.setFirstName(addressBookForm.getFirstName());
                    existingAddressBook.setLastName(addressBookForm.getLastName());
                    existingAddressBook.setPhoneNumber(addressBookForm.getPhoneNumber());
                    existingAddressBook.setAddress(addressBookForm.getAddress());
                    existingAddressBook.setEmailAddress(addressBookForm.getEmailAddress());
                    return addressBookRepository.save(existingAddressBook);
                })
                .map(updateAddressBook -> new ResponseEntity<>(updateAddressBook, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/api/v1/addressbooks/{id}")
    public Mono<ResponseEntity<Void>> deleteAddressBookEntry(@PathVariable(value = "id") String addressBookId) {
    	logger.debug("Delete address book entry by id [{}]", addressBookId);
        return addressBookRepository.findById(addressBookId)
                .flatMap(existingAddressBook ->
                        addressBookRepository.delete(existingAddressBook)
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // AddressBooks are Sent to the client as Server Sent Events
    @GetMapping(value = "/api/v1/stream/addressBooks", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AddressBook> streamAllTweets() { 
    	logger.debug("Streaming address book entries..");
        return addressBookRepository.findAll();
    }
	

}
