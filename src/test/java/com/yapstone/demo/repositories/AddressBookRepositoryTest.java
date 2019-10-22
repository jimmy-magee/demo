package com.yapstone.demo.repositories;


import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.yapstone.demo.domain.AddressBook;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
/**
 * Integration test for {@link AddressBookRepository} using Project Reactor types and operators.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest({
	"com.yapstone.demo=INFO",
	"logging.level.com.naturalprogrammer=INFO", // logging.level.root=ERROR does not work: https://stackoverflow.com/questions/49048298/springboottest-not-overriding-logging-level
	"logging.level.org.springframework=INFO",
	"logging.level.org.springframework.web=ERROR",
	"spring.data.mongodb.database=test"
})
public class AddressBookRepositoryTest {
	
	@Autowired
	AddressBookRepository repository;

	@Autowired
	ReactiveMongoOperations operations;
	
	@Before
	public void setUp() {

		Mono<MongoCollection<Document>> recreateCollection = operations.collectionExists(AddressBook.class) 
				.flatMap(exists -> exists ? operations.dropCollection(AddressBook.class) : Mono.just(exists)) 
				.then(operations.createCollection(AddressBook.class, CollectionOptions.empty()));

		StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();

		Flux<AddressBook> insertAll = operations.insertAll(Flux.just(getFirstAddressBookEntry()).collectList());

		StepVerifier.create(insertAll).expectNextCount(1).verifyComplete();
	}
	
	/**
	 * This sample performs a count, inserts data and performs a count again using reactive operator chaining. It prints
	 * the two counts ({@code 4} and {@code 6}) to the console.
	 */
	
	@Test
	public void shouldInsertAndCountData() {

		Mono<Long> saveAndCount = repository.count() //
				.doOnNext(System.out::println) //
				.thenMany(repository.saveAll(Flux.just(getSecondAddressBookEntry()))) //
				.last() //
				.flatMap(v -> repository.count()) //
				.doOnNext(System.out::println);

		StepVerifier.create(saveAndCount).expectNext(2L).verifyComplete();

		//StepVerifier.create(repository.deleteById("2")).verifyComplete();
	}
	
	
	private AddressBook getFirstAddressBookEntry() {
    
        return AddressBook.builder()
        		.id("1")
        		.firstName("Peppa")
				.lastName("Pig")
				.phoneNumber("087 439 6171")
				.address("Drogheda, Co. Louth")
				.emailAddress("peppa@yapstone.com")
				.build();
	}
	
	private AddressBook getSecondAddressBookEntry() {
	    
        return AddressBook.builder()
        		.id("2")
        		.firstName("George")
				.lastName("Pig")
				.phoneNumber("087 439 6272")
				.address("Drogheda, Co. Louth")
				.emailAddress("george@yapstone.com")
				.build();
	}

}
