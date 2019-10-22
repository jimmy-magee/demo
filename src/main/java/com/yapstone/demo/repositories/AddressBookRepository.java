package com.yapstone.demo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.yapstone.demo.domain.AddressBook;


@Repository
public interface AddressBookRepository extends ReactiveMongoRepository<AddressBook, String> {


}
