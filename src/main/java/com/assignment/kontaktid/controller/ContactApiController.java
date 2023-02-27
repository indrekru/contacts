package com.assignment.kontaktid.controller;

import com.assignment.kontaktid.controller.request.CreateContactRequest;
import com.assignment.kontaktid.dto.ContactDto;
import com.assignment.kontaktid.model.Contact;
import com.assignment.kontaktid.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactApiController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public List<ContactDto> findAllContacts() {
        return mapDto(contactRepository.findAll());
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public void saveContact(@RequestBody CreateContactRequest request) {
        contactRepository.save(Contact.builder()
                .name(request.name())
                .codeName(request.codeName())
                .phone(request.phone())
                .build());
    }

    private List<ContactDto> mapDto(List<Contact> contacts) {
        return contacts.stream()
                .map(contact -> ContactDto.builder()
                        .id(contact.id())
                        .name(contact.name())
                        .codeName(contact.codeName())
                        .phone(contact.phone())
                        .createTime(contact.createTime())
                        .updateTime(contact.updateTime())
                        .deleteTime(contact.deleteTime())
                        .build())
                .toList();
    }

}
