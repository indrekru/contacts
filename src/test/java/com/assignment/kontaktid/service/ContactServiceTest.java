package com.assignment.kontaktid.service;

import com.assignment.kontaktid.BaseIntTest;
import com.assignment.kontaktid.model.Contact;
import com.assignment.kontaktid.utils.EncryptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContactServiceTest extends BaseIntTest {

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private ContactService contactService;

    @Test
    @DisplayName("finds encrypted records")
    public void findsEncryptedRecord() throws Exception {

        contactService.save(Contact.builder()
                .name(encryptionService.encrypt("James Bond"))
                .codeName(encryptionService.encrypt("Top secret"))
                .phone(encryptionService.encrypt("+49 98765"))
                .build());

        contactService.save(Contact.builder()
                .name(encryptionService.encrypt("Jüri Lätist"))
                .codeName(encryptionService.encrypt("Mingi väga salajane info"))
                .phone(encryptionService.encrypt("+372 12345"))
                .build());

        String query = "Läti";

        List<Contact> results = contactService.search(query);
        assertThat(results.size()).isEqualTo(1);
        Contact contact = results.get(0);
        assertThat(encryptionService.decrypt(contact.name())).isEqualTo("Jüri Lätist");
        assertThat(encryptionService.decrypt(contact.codeName())).isEqualTo("Mingi väga salajane info");
        assertThat(encryptionService.decrypt(contact.phone())).isEqualTo("+372 12345");
    }
}
