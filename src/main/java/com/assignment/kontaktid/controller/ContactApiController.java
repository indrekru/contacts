package com.assignment.kontaktid.controller;

import com.assignment.kontaktid.controller.request.CreateContactRequest;
import com.assignment.kontaktid.dto.ContactDto;
import com.assignment.kontaktid.model.Contact;
import com.assignment.kontaktid.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactApiController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public List<ContactDto> findAllContacts() {
        return mapDto(contactService.findAll());
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public void saveContact(@RequestBody CreateContactRequest request) {
        contactService.save(Contact.builder()
                .name(request.name())
                .codeName(request.codeName())
                .phone(request.phone())
                .build());
    }

    @GetMapping("/search")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<ContactDto> search(@RequestParam("q") String query) throws Exception {
        return mapDto(contactService.findContactsLike(query));
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
