package com.assignment.kontaktid.repository;

import com.assignment.kontaktid.model.Contact;

import java.util.List;
import java.util.UUID;

public interface ContactRepository {
    List<Contact> findAll();
    List<Contact> findAllByIdsIn(List<UUID> ids);
    void save(Contact contact);
}
