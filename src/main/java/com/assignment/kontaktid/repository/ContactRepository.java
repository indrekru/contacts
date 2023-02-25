package com.assignment.kontaktid.repository;

import com.assignment.kontaktid.model.Contact;

import java.util.List;

public interface ContactRepository {
    List<Contact> findAll();
    void save(Contact contact);
}
