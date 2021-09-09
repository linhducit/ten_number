package com.nld.contacts;

public class Contacts {
    private String id, contactsName, contactsNumber;

    public Contacts(String id, String contactsName, String contactsNumber) {
        this.id = id;
        this.contactsName = contactsName;
        this.contactsNumber = contactsNumber;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsNumber() {
        return contactsNumber;
    }

    public void setContactsNumber(String contactsNumber) {
        this.contactsNumber = contactsNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
