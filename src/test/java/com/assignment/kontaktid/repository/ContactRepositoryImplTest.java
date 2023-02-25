package com.assignment.kontaktid.repository;

import com.assignment.kontaktid.model.Contact;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class ContactRepositoryImplTest {

	@Autowired
	private ContactRepository contactRepository;

	@Container
	private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:14.1-alpine")
			.withInitScript("db/migration/V2023_02_01__init.sql");

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", database::getJdbcUrl);
		registry.add("spring.datasource.username", database::getUsername);
		registry.add("spring.datasource.password", database::getPassword);
	}

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
