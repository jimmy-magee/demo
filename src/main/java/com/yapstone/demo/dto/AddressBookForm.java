package com.yapstone.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object to represent an Address Book form.
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookForm {
	
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private String emailAddress;

}
