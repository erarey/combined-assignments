package com.cooksys.ftd.assignments.file.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Student {
    private Contact contact;
@XmlElement
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
    public Student()
    {
    	contact = null;
    }
    
    public Student(Contact c)
    {
    	setContact(c);
    	
    }
}
