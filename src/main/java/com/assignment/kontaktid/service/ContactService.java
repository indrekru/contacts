package com.assignment.kontaktid.service;

import com.assignment.kontaktid.model.Contact;
import com.assignment.kontaktid.repository.ContactRepository;
import com.assignment.kontaktid.utils.EncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final EncryptionService encryptionService;
    private final ContactRepository contactRepository;

    public List<Contact> search(String query) {
        List<Contact> decryptedContacts = contactRepository.findAll().stream()
                .map(contact -> {
                            try {
                                return contact
                                        .withName(encryptionService.decrypt(contact.name()))
                                        .withCodeName(encryptionService.decrypt(contact.codeName()))
                                        .withPhone(encryptionService.decrypt(contact.phone()));
                            } catch (Exception e) {
                                log.warn("Failed decrypting values", e);
                                throw new RuntimeException(e);
                            }
                        }
                )
                .toList();

        List<UUID> ids = decryptedContacts.stream()
                .filter(contact -> contact.name().contains(query)
                        || contact.codeName().contains(query)
                        || contact.phone().contains(query))
                .map(contact -> contact.id())
                .toList();

        return ids.isEmpty() ? List.of() : contactRepository.findAllByIdsIn(ids);
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public void save(Contact contact) {
        contactRepository.save(contact);
    }
}
