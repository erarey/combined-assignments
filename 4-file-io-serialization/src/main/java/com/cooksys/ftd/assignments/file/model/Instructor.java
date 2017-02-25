package com.cooksys.ftd.assignments.file.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class Instructor {
    private Contact contact;

    public Instructor()
    {
    	contact = null;
    }
    
    public Instructor(Contact c)
    {
    	contact = c;
    	
    }
    
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
