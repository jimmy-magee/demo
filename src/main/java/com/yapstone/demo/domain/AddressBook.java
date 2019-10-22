package com.yapstone.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An entity to represent an Address Book
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "address_books")
public class AddressBook {
	
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private String emailAddress;

}
