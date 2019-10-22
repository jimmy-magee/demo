package com.yapstone.demo.domain;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class AddressBookTest {
	
	@Test
	public void testCreateAddressBook() {
	     AddressBook addressBook = new AddressBook("123", "horrid", "henry", "083 80808020", "Drogheda, Co. Louth", "horidhenry@yatsone.com");
         Assertions.assertThat(addressBook.getId()).isEqualToIgnoringWhitespace("123");
         Assertions.assertThat(addressBook.getFirstName()).isEqualToIgnoringWhitespace("horrid");
         Assertions.assertThat(addressBook.getLastName()).isEqualToIgnoringWhitespace("henry");
         Assertions.assertThat(addressBook.getPhoneNumber()).isEqualToIgnoringWhitespace("083 80808020");
         Assertions.assertThat(addressBook.getAddress()).isEqualToIgnoringWhitespace("Drogheda, Co. Louth");
         Assertions.assertThat(addressBook.getEmailAddress()).isEqualToIgnoringWhitespace("horidhenry@yatsone.com");
	}
	

}
