package com.assignment.kontaktid.repository;

import com.assignment.kontaktid.BaseIntTest;
import com.assignment.kontaktid.model.Contact;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ContactRepositoryImplTest extends BaseIntTest {

	@Autowired
	private ContactRepository contactRepository;

	@Test
	@DisplayName("saves and finds all contacts")
	void savesAndFindsAllContacts() {
		contactRepository.save(Contact.builder()
						.name(randomUUID().toString())
						.codeName(randomUUID().toString())
						.phone(randomUUID().toString())
				.build());
		List<Contact> results = contactRepository.findAll();

		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).id()).isNotNull();
		assertThat(results.get(0).name()).isNotNull();
		assertThat(results.get(0).codeName()).isNotNull();
		assertThat(results.get(0).phone()).isNotNull();
		assertThat(results.get(0).createTime()).isNotNull();
		assertThat(results.get(0).updateTime()).isNotNull();
		assertThat(results.get(0).deleteTime()).isNull();
	}
}
