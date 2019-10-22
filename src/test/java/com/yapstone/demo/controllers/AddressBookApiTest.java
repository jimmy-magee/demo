package com.yapstone.demo.controllers;

import com.yapstone.demo.domain.AddressBook;
import com.yapstone.demo.dto.AddressBookForm;
import com.yapstone.demo.repositories.AddressBookRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressBookApiTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
    AddressBookRepository addressBookRepository;

	@Test
	public void testCreateAddressBook() {
		AddressBook addressBook = AddressBook.builder()
				.firstName("bob")
				.lastName("builder")
				.phoneNumber("00353879766080")
				.address("Up Wee County!!")
				.emailAddress("bob@yapstone.com")
				.build();
				
		webTestClient.post().uri("/api/v1/addressbooks")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(addressBook), AddressBook.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo("bob")
                .jsonPath("$.lastName").isEqualTo("builder")
                .jsonPath("$.phoneNumber").isEqualTo("00353879766080")
                .jsonPath("$.address").isEqualTo("Up Wee County!!")
                .jsonPath("$.emailAddress").isEqualTo("bob@yapstone.com");
	}

	@Test
    public void testGetAllAddressBooks() {
	    webTestClient.get().uri("/api/v1/addressbooks")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(AddressBook.class);
    }
	
    @Test
    public void testGetSingleAddressBook() {
    	AddressBook addressBook = AddressBook.builder()
				.firstName("bob")
				.lastName("builder")
				.phoneNumber("00353879766080")
				.address("Up Wee County!!")
				.emailAddress("bob@yapstone.com")
				.build();
    	
        webTestClient.get()
                .uri("/api/v1/addressbooks/{id}", Collections.singletonMap("id", addressBook.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateAddressBook() {
    	
    	AddressBook addressBook = AddressBook.builder()
				.firstName("bob")
				.lastName("builder")
				.phoneNumber("00353879766080")
				.address("Up Wee County!!")
				.emailAddress("bob@yapstone.com")
				.build();
    	
        AddressBook savedAddressBook = addressBookRepository.save(addressBook).block();

        AddressBookForm newAddressBookData = AddressBookForm.builder()
				.firstName("bob")
				.lastName("builder 2")
				.phoneNumber("00353879766082")
				.address("Up Wee County..")
				.emailAddress("bob.builder@yapstone.com")
				.build();
        
        webTestClient.put()
                .uri("/api/v1/addressbooks/{id}", Collections.singletonMap("id", savedAddressBook.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newAddressBookData), AddressBookForm.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("bob")
                .jsonPath("$.lastName").isEqualTo("builder 2")
                .jsonPath("$.phoneNumber").isEqualTo("00353879766082")
                .jsonPath("$.address").isEqualTo("Up Wee County..")
                .jsonPath("$.emailAddress").isEqualTo("bob.builder@yapstone.com");
    }

    @Test
    public void testDeleteAddressBook() {
    	
    	AddressBook addressBook = AddressBook.builder()
				.firstName("bob")
				.lastName("builder")
				.phoneNumber("00353879766080")
				.address("Up Wee County!!")
				.emailAddress("bob@yapstone.com")
				.build();
    	
	    AddressBook savedAddressBook = addressBookRepository.save(addressBook).block();

	    webTestClient.delete()
                .uri("/api/v1/addressbooks/{id}", Collections.singletonMap("id",  savedAddressBook.getId()))
                .exchange()
                .expectStatus().isOk();
    }
}
